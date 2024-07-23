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
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kuneosu.mintoners.databinding.FragmentMatchMainBinding
import com.kuneosu.mintoners.ui.adapter.MatchMainPagerAdapter
import com.kuneosu.mintoners.ui.customview.MatchInfoDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
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

        binding.matchMainShareButton.setOnClickListener {
            getBitmapFromScrollView(binding.matchMainScrollView) { bitmap ->
                bitmap?.let {
                    screenShot(it)
                }
            }
        }

        binding.matchMainRoot.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()
                hideKeyboard()
                v.performClick()
            }
            false
        }


        return binding.root
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
        binding.matchMainTopInfo.setOnClickListener {
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

    private var isDouble = false
    private fun backPressToast() {
        Toast.makeText(requireContext(), "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when {
                isDouble -> {
                    binding.root.clearFocus()
                    matchViewModel.applyPlayerList()
                    matchViewModel.applyGameList()
                    matchViewModel.updateMatchByNumber(matchViewModel.match.value?.matchNumber!!)
                    Toast.makeText(context, "대회 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }
            backPressToast()

            isDouble = true
            Handler().postDelayed({
                isDouble = false
            }, 2000)
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


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getBitmapFromView(view: View, callback: (Bitmap?) -> Unit) {
//
//        activity?.window?.let { window ->
//            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//            // 비트맵 생성
//
//            val locationOfViewInWindow = IntArray(2)
//            view.getLocationInWindow(locationOfViewInWindow)
//            // 해당 뷰의 좌표를 계산하여 배열에 x, y가 반환
//
//            try {
//                PixelCopy.request(
//                    window,
//                    Rect(
//                        locationOfViewInWindow[0],
//                        locationOfViewInWindow[1],
//                        locationOfViewInWindow[0] + view.width,
//                        locationOfViewInWindow[1] + view.height
//                    ),
//                    bitmap, { copyResult ->
//                        if (copyResult == PixelCopy.SUCCESS) callback.invoke(bitmap)
//                        else callback.invoke(null)
//                    }, Handler(Looper.getMainLooper())
//                )
//            } catch (e: IllegalArgumentException) {
//                callback.invoke(null)
//            }
//        }
//    }
//
//    private fun screenShot(bitmap: Bitmap) {
//        try {
//            val cachePath = File(context?.cacheDir, "images")
//            cachePath.mkdirs()
//
//            val stream = FileOutputStream("$cachePath/image.png")
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            stream.close()
//
//            val newFile = File(cachePath, "image.png")
//            val contentUri: Uri = FileProvider.getUriForFile(
//                requireContext(),
//                "com.kuneosu.mintoners.fileprovider", newFile
//            )
//
//            val sharingIntent = Intent(Intent.ACTION_SEND)
//
//            sharingIntent.type = "image/png"
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
//            startActivity(Intent.createChooser(sharingIntent, "Share image using"))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}