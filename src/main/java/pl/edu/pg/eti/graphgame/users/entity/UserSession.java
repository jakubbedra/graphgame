package pl.edu.pg.eti.graphgame.users.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_sessions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserSession {

    @Id
    private String token;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
// 	@NotNull
	private User user;
	private String expirationDatetime;

}
