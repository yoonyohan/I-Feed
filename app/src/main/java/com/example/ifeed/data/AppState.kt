package com.example.ifeed.data

data class AppState(
    // Log In
    val logInUserName: String = "",
    val logInPassword: String = "",
    val isEmpty: Boolean = false,
    val isLoggedIn: Boolean = false,

    // Sign Up
    val firstName: String = "",
    val lastName: String= "",
    val middleName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val profileUrl: String = "",
    val profileImageLoad: Boolean = false,
    val profileImageUrl: String = "",
    val emailAddressOn: Boolean = true,
    val phoneNumberOn: Boolean = false,
    val toAccountCreation: Boolean = false,
    val updateProfileImageWithDataBases: Boolean = false,
    val alert: String = "",
    val isLoading: Boolean = false,
    val userName: String = "",
    val accountCreationSuccess: Boolean = false,
    val toLoggedIn: Boolean = false,

    // Add New Post
    val content: String = "",
    val userID: Int? = null,
    val postCreationSuccess: Boolean = false,

    // Feed
    val postFireList: List<FeedPost> = emptyList(),
    val postIsLoading: Boolean = false,
)