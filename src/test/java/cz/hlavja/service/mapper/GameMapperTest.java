package cz.hlavja.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GameMapperTest {

    private GameMapper gameMapper;

    @BeforeEach
    public void setUp() {
        gameMapper = new GameMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(gameMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(gameMapper.fromId(null)).isNull();
    }
}
