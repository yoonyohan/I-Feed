package com.example.ifeed.application

import com.example.ifeed.data.Repository

interface AppContainer {
    val offlineRepository: Repository
}