package springbootkotlin.toy.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import springbootkotlin.toy.entity.QUser
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.repository.custom.UserRepositoryCustom

@Repository
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun findSpecialUsers(name: String, email: String): List<User> {
        val user = QUser.user
        return queryFactory
            .selectFrom(user)
            .where(
                user.name.eq(name),
                user.email.eq(email)
            )
            .fetch()
    }
}