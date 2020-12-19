package cz.hlavja.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

/**
 * A DTO for the {@link cz.hlavja.domain.Friend} entity.
 */
@ApiModel(description = "The Friend entity.\n@author hlavja")
public class FriendDTO implements Serializable {

    private Long id;

    private Long userId;

    private Long friendWithId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendWithId() {
        return friendWithId;
    }

    public void setFriendWithId(Long userId) {
        this.friendWithId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FriendDTO)) {
            return false;
        }

        return id != null && id.equals(((FriendDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FriendDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", friendWithId=" + getFriendWithId() +
            "}";
    }
}
