package com.example.orata.utils.auth

class AuthSnapshot {
    private var dbID = 0
    private var id = 0
    private var name: String = ""
    private var token: String = ""
    private var level: String = ""
    private var address: String = ""
    private var phone: String = ""
    private var email: String = ""
    private var isLogin = 0


    fun AuthSnapshot() {}

    fun getDbID(): Int {
        return dbID
    }

    fun setDbID(dbID: Int) {
        this.dbID = dbID
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setLevel(level: String) {
        this.level = level
    }

    fun setAddress(address: String) {
        this.address = address
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun getEmail(): String {
        return email
    }

    fun getAddress(): String {
        return address
    }

    fun getPhone(): String {
        return phone
    }

    fun getLevel(): String {
        return level
    }

    fun getToken(): String {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun getIsLogin(): Int {
        return isLogin
    }

    fun setIsLogin(isLogin: Int) {
        this.isLogin = isLogin
    }

    override fun toString(): String {
        return "AuthSnapshot{" +
                "dbID=" + dbID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", level='" + level + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", isLogin=" + isLogin +
                '}'
    }
}