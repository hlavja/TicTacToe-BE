package cz.hlavja.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cz.hlavja.web.rest.TestUtil;

public class FriendDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FriendDTO.class);
        FriendDTO friendDTO1 = new FriendDTO();
        friendDTO1.setId(1L);
        FriendDTO friendDTO2 = new FriendDTO();
        assertThat(friendDTO1).isNotEqualTo(friendDTO2);
        friendDTO2.setId(friendDTO1.getId());
        assertThat(friendDTO1).isEqualTo(friendDTO2);
        friendDTO2.setId(2L);
        assertThat(friendDTO1).isNotEqualTo(friendDTO2);
        friendDTO1.setId(null);
        assertThat(friendDTO1).isNotEqualTo(friendDTO2);
    }
}
