package springbootkotlin.toy.user.login.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import springbootkotlin.toy.entity.QUser
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.user.login.repository.custom.UserRepositoryCustom

@Repository
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun findByUserIdAndPassword(userId: String, password: String): User? {
        val user = QUser.user
        return queryFactory
            .selectFrom(user)
            .where(
                user.userId.eq(userId),
                user.password.eq(password)
            )
            .fetchOne()
    }
}