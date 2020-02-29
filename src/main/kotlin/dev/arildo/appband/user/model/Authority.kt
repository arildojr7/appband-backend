package dev.arildo.appband.user.model

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {
    ROLE_GUEST, ROLE_USER, ROLE_ADMIN;

    override fun getAuthority(): String {
        return this.toString()
    }

    companion object {
        fun fromArray(array: Array<String>): Set<Authority> {
            val set: MutableSet<Authority> = mutableSetOf()
            array.forEach {
                set.add(valueOf(it))
            }
            return set
        }
    }

}