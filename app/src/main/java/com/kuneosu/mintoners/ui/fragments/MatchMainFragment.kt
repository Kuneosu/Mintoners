package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainBinding
import com.kuneosu.mintoners.ui.adapter.MatchMainPagerAdapter
import com.kuneosu.mintoners.ui.customview.MatchBackDialog
import com.kuneosu.mintoners.ui.customview.MatchInfoDialog
import com.kuneosu.mintoners.ui.customview.OneMoreWarningDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.GuideToolTip
import com.kuneosu.mintoners.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MatchMainFragment : Fragment() {
    private var _binding: FragmentMatchMainBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()
    private var lastClickTime = 0L


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainBinding.inflate(inflater, container, false)

        displayInfoSetting()
        infoDialogSetting()
        moveButtonSetting()
        initPager()

        binding.matchMainSwipeRefresh.setOnRefreshListener {
            binding.matchMainSwipeRefresh.isRefreshing = false
            binding.matchMainViewPager.adjustHeight()
            matchViewModel.updatePoint(string = "Swipe Refresh")
        }

        binding.matchMainHelp.setOnClickListener {
            mainFragmentGuide()
        }

        binding.matchMainTopBack.setOnClickListener {
            callback.handleOnBackPressed()
        }

        sharedButtonSetting()

        binding.matchMainRoot.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()
                hideKeyboard()
                v.performClick()
            }
            false
        }

        checkFirstEntrance()

        oneMoreButtonSettingWithMatchMode()


        return binding.root
    }

    private fun checkFirstEntrance() {
        // PreferencesManager 인스턴스 생성
        val preferencesManager = PreferencesManager(requireContext())
        // 현재 프래그먼트의 이름을 키로 사용
        val fragmentName = this::class.simpleName

        // 최초 진입 여부 확인
        if (fragmentName != null && preferencesManager.isFirstTimeLaunch(fragmentName)) {
            // 최초 진입이므로 팝업 가이드 표시
            mainFragmentGuide()
            // 최초 진입 상태를 false로 변경
            preferencesManager.setFirstTimeLaunch(fragmentName, false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sharedButtonSetting() {
        binding.matchMainShareButton.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return@setOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()

            getBitmapFromScrollView(binding.matchMainScrollView) { bitmap ->
                bitmap?.let {
                    screenShot(it)
                }
            }
        }
    }

    private fun oneMoreButtonSettingWithMatchMode() {
        if (matchViewModel.matchMode.value == 0) {
            binding.matchMainOneMoreButton.setOnClickListener {
                val dialog = OneMoreWarningDialog { oneMoreGameGenerate() }
                dialog.show(childFragmentManager, "OneMoreWarningDialog")

            }
        } else {
            binding.matchMainOneMoreButton.visibility = View.GONE
            binding.matchMainEndButton.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                //넓이, 높이
                height = resources.getDimensionPixelSize(R.dimen.height_match_process_move_button)

                setMargins(40, 120, 40, 120) //margin left, top, right, bottom 한번에 적용

                //ConstraintSet 제약조건
                bottomToBottom = ConstraintSet.PARENT_ID //부모 View
                endToEnd = ConstraintSet.PARENT_ID
                startToStart = ConstraintSet.PARENT_ID     //대상 View id
                topToBottom = binding.matchMainViewPager.id
            }
        }
    }

    private fun oneMoreGameGenerate() {
        matchViewModel.oneMoreGame()
        matchViewModel.updatePoint(string = "One More")
        binding.matchMainViewPager.adjustHeight()
    }

    private fun mainFragmentGuide() {
        val backPressedCallbackOnGuide = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    requireContext(),
                    "가이드를 완료하려면 화면을 터치해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallbackOnGuide
        )
        binding.matchMainTouchInterceptor.isTouchIntercepted = true
        binding.matchMainTouchInterceptor.visibility = View.VISIBLE
        GuideToolTip().createGuide(
            context = requireContext(),
            text = "대회명을 눌러 대회 정보를 확인할 수 있습니다.",
            anchor = binding.matchMainTopTitle,
            gravity = Gravity.BOTTOM,
            shape = "oval",
            dismiss = {
                GuideToolTip().createGuide(
                    context = requireContext(),
                    text = "대진 수정 버튼을 눌러 이전 단계로 돌아가 대회 정보를 수정할 수 있습니다.",
                    anchor = binding.matchMainEditButton,
                    gravity = Gravity.BOTTOM,
                    shape = "rectangular",
                    dismiss = {
                        GuideToolTip().createGuide(
                            context = requireContext(),
                            text = "대진 공유 버튼을 눌러 대진표나 현재 순위를 이미지로 공유할 수 있습니다.",
                            anchor = binding.matchMainShareButton,
                            gravity = Gravity.BOTTOM,
                            shape = "rectangular",
                            dismiss = {
                                GuideToolTip().createGuide(
                                    context = requireContext(),
                                    text = "대진표와 순위를 확인할 수 있습니다.\n\n순서 영역의 숫자를 터치하면 해당 대진표를 잠금 상태로 변경할 수 있습니다." +
                                            "\n\n대진을 꾹 눌러 드래그하면 대진 순서를 조정할 수 있습니다.\n\n" +
                                            "화면을 아래로 당겨 대진표와 순위표를 최신화 할 수 있습니다.",
                                    anchor = binding.matchMainViewPager,
                                    gravity = Gravity.TOP,
                                    shape = "rectangular",
                                    dismiss = {
                                        binding.matchMainScrollView.scrollY = 3000
                                        GuideToolTip().createGuide(
                                            context = requireContext(),
                                            text = "섞어서 한번 더하기는 파트너를 섞어서 대진표를 초기화 할 수 있는 기능입니다.\n기존의 대진표와 순위는 제거되는 점에 유의하세요 !",
                                            anchor = binding.matchMainOneMoreButton,
                                            gravity = Gravity.TOP,
                                            shape = "rectangular",
                                            dismiss = {
                                                GuideToolTip().createGuide(
                                                    context = requireContext(),
                                                    text = "경기 종료 버튼을 눌러 대회를 종료하고 저장할 수 있습니다.",
                                                    anchor = binding.matchMainEndButton,
                                                    gravity = Gravity.TOP,
                                                    shape = "rectangular",
                                                    dismiss = {
                                                        binding.matchMainScrollView.scrollY = 0
                                                        requireActivity().onBackPressedDispatcher.addCallback(
                                                            viewLifecycleOwner,
                                                            callback
                                                        )
                                                        binding.matchMainTouchInterceptor.isTouchIntercepted =
                                                            false
                                                        binding.matchMainTouchInterceptor.visibility =
                                                            View.GONE
                                                    })
                                            })

                                    }
                                )
                            }
                        )
                    }
                )
            }
        )

    }

    private fun setButtonsVisibility(visibility: Int) {
        // DisplayMetrics dm = getResources().getDisplayMetrics();
        //    int size = Math.round(20 * dm.density);

        val dm = resources.displayMetrics
        val size = (60 * dm.density).toInt()

        if (visibility == View.GONE) {
            binding.matchMainContent.setPadding(0, 0, 0, 0)
        } else {
            binding.matchMainContent.setPadding(0, 0, 0, size)
        }

        binding.matchMainEditButton.visibility = visibility
        binding.matchMainShareButton.visibility = visibility
        binding.matchMainEndButton.visibility = visibility
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun initPager() {
        val viewPager = binding.matchMainViewPager
        val tabLayout = binding.matchMainTab

        val fragmentList = listOf(
            MatchMainListFragment(),
            MatchMainRankFragment(),
        )

        viewPager.adapter =
            MatchMainPagerAdapter(fragmentList, this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewPager.adjustHeight()
            }
        })

        matchViewModel.updateCount.observe(viewLifecycleOwner, Observer {
            val currentItem = viewPager.currentItem
            viewPager.adapter =
                MatchMainPagerAdapter(fragmentList, this)
            viewPager.setCurrentItem(currentItem, false)
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "대진표"
                1 -> tab.text = "현재 순위"
            }
        }.attach()

        binding.matchMainTab.addOnTabSelectedListener(object :
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                Handler().postDelayed({
                    viewPager.adjustHeight()
                }, 100)
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

    }

    fun ViewPager2.adjustHeight() {
        val view = (getChildAt(0) as? RecyclerView)?.layoutManager?.findViewByPosition(currentItem)
        view?.post {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            val height = view.measuredHeight
            if (layoutParams.height != height) {
                layoutParams = layoutParams.apply {
                    this.height = height
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayInfoSetting() {
        binding.matchMainTopTitle.text = matchViewModel.match.value?.matchName ?: ""
        binding.matchMainCountText.text = "" +
                "참가 인원 : ${matchViewModel.match.value?.matchPlayers?.size ?: 0}명\n" +
                "총 경기 수 : ${matchViewModel.match.value?.matchList?.size ?: 0}경기"
    }

    private fun moveButtonSetting() {
        binding.matchMainEditButton.setOnClickListener {
            findNavController().popBackStack()
            matchViewModel.updateMatchState(2)
        }
        binding.matchMainEndButton.setOnClickListener {
            binding.root.clearFocus()
            matchViewModel.applyPlayerList()
            matchViewModel.applyGameList()
            matchViewModel.updateMatchByNumber(matchViewModel.match.value?.matchNumber!!)
            Toast.makeText(context, "대회 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun infoDialogSetting() {
        binding.matchMainTopTitle.setOnClickListener {
            val infoTitle = "대회명 : ${matchViewModel.match.value?.matchName ?: ""}"
            val infoDate = "대회일자 : ${dateToString(matchViewModel.match.value?.matchDate)}"
            val infoPoint = "승점 : ${matchViewModel.match.value?.matchPoint ?: ""}"
            val infoType =
                "경기방식 : ${if (matchViewModel.match.value?.matchType == "double") "복식" else "단식"}"
            val infoPlayerCount = "참가자 수 : ${matchViewModel.match.value?.matchPlayers?.size ?: 0}"
            val infoGameCount = "총 경기 수 : ${matchViewModel.match.value?.matchList?.size ?: 0}"


            val dialog = MatchInfoDialog(
                infoTitle,
                infoDate,
                infoPoint,
                infoType,
                infoPlayerCount,
                infoGameCount
            )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "MatchInfoDialog")
        }
    }


    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            binding.root.clearFocus()
            val dialog = MatchBackDialog("main", matchViewModel)
            dialog.show(childFragmentManager, "MatchBackDialog")
        }
    }

    private fun dateToString(date: Date?): String {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        return sdf.format(date!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getBitmapFromScrollView(scrollView: NestedScrollView, callback: (Bitmap?) -> Unit) {
        setButtonsVisibility(View.GONE)

        val height = scrollView.getChildAt(0).height
        val width = scrollView.width
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        callback.invoke(bitmap)

        setButtonsVisibility(View.VISIBLE)
    }

    private fun screenShot(bitmap: Bitmap) {
        try {
            val cachePath = File(context?.cacheDir, "images")
            cachePath.mkdirs()

            val stream = FileOutputStream("$cachePath/image.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val newFile = File(cachePath, "image.png")
            val contentUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.kuneosu.mintoners.fileprovider", newFile
            )

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/png"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(sharingIntent, "Share image using"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}