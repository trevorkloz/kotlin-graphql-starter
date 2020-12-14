package com.example.backend.graphql

class Search {

    var firstname: String? = null
    var lastname: String? = null
    var street: String? = null
    var city: String? = null
    var state: String? = null

    var _fetch: Int = 25
    var _pageNum: Int = 1

    var _sortBy: SortBy? = null
    var _sortDirection: SortDirection? = null
}