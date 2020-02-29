package dev.arildo.appband.user.repository

import dev.arildo.appband.user.model.ApplicationUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationUserRepository : MongoRepository<ApplicationUser, String> {

    fun findByEmail(email: String): ApplicationUser?

}