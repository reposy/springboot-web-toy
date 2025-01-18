package springbootkotlin.toy.entity
import jakarta.persistence.*

@Entity
@Table(name = "USERS")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String
)