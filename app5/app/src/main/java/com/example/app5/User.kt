package com.example.app5

class User {
    val name: String
    val lastname: String
    val login: String
    val pwd: String
    val dob: String

    constructor(
        name: String,
        lastname: String,
        login: String,
        pwd: String,
        dob: String,){
        this.name = name
        this.lastname = lastname
        this.login = login
        this.pwd = pwd
        this.dob = dob
    }
}