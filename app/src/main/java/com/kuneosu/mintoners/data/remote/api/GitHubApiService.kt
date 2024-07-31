package com.kuneosu.mintoners.data.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("/repos/{owner}/{repo}/contents/{path}")
    fun getFiles(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Call<List<GitHubFile>>

    @GET("/repos/{owner}/{repo}/contents/{path}")
    fun getFileContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Call<GitHubFile>
}

data class GitHubFile(
    val name: String,
    val downloadUrl: String,
    val content: String // Base64 encoded content
)