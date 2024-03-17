package ch.learnup.backend.security

import ch.learnup.backend.persistence.tables.pojos.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LearnupUserDetails(val user: User) : UserDetails {
    override fun getAuthorities() = listOf<GrantedAuthority>()
    override fun getPassword() = null
    override fun getUsername() = user.eMail
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
