package cz.hlavja.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FriendMapperTest {

    private FriendMapper friendMapper;

    @BeforeEach
    public void setUp() {
        friendMapper = new FriendMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(friendMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(friendMapper.fromId(null)).isNull();
    }
}
