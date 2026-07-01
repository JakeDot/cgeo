package cgeo.geocaching.connector;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserInfoTest {

    @Test
    public void testBasicConstructor() {
        final UserInfo userInfo = new UserInfo("TestUser", 100, UserInfo.UserInfoStatus.SUCCESSFUL);

        assertThat(userInfo.getName()).isEqualTo("TestUser");
        assertThat(userInfo.getFinds()).isEqualTo(100);
        assertThat(userInfo.getStatus()).isEqualTo(UserInfo.UserInfoStatus.SUCCESSFUL);
        assertThat(userInfo.getRemainingFavoritePoints()).isEqualTo(-1);
    }

    @Test
    public void testFullConstructor() {
        final UserInfo userInfo = new UserInfo("TestUser", 100, UserInfo.UserInfoStatus.SUCCESSFUL, 50);

        assertThat(userInfo.getName()).isEqualTo("TestUser");
        assertThat(userInfo.getFinds()).isEqualTo(100);
        assertThat(userInfo.getStatus()).isEqualTo(UserInfo.UserInfoStatus.SUCCESSFUL);
        assertThat(userInfo.getRemainingFavoritePoints()).isEqualTo(50);
    }

    @Test
    public void testZeroFinds() {
        final UserInfo userInfo = new UserInfo("NewUser", 0, UserInfo.UserInfoStatus.SUCCESSFUL);
        assertThat(userInfo.getFinds()).isEqualTo(0);
    }

    @Test
    public void testNegativeFavoritePoints() {
        final UserInfo userInfo = new UserInfo("TestUser", 100, UserInfo.UserInfoStatus.SUCCESSFUL);
        assertThat(userInfo.getRemainingFavoritePoints()).isEqualTo(-1);
    }

    @Test
    public void testZeroFavoritePoints() {
        final UserInfo userInfo = new UserInfo("TestUser", 100, UserInfo.UserInfoStatus.SUCCESSFUL, 0);
        assertThat(userInfo.getRemainingFavoritePoints()).isEqualTo(0);
    }

    @Test
    public void testEmptyUsername() {
        final UserInfo userInfo = new UserInfo("", 0, UserInfo.UserInfoStatus.FAILED);
        assertThat(userInfo.getName()).isEmpty();
        assertThat(userInfo.getStatus()).isEqualTo(UserInfo.UserInfoStatus.FAILED);
    }

    @Test
    public void testFailedStatus() {
        final UserInfo userInfo = new UserInfo("TestUser", 0, UserInfo.UserInfoStatus.FAILED);
        assertThat(userInfo.getStatus()).isEqualTo(UserInfo.UserInfoStatus.FAILED);
    }

    @Test
    public void testNotRetrievedStatus() {
        final UserInfo userInfo = new UserInfo("TestUser", 0, UserInfo.UserInfoStatus.NOT_RETRIEVED);
        assertThat(userInfo.getStatus()).isEqualTo(UserInfo.UserInfoStatus.NOT_RETRIEVED);
    }
}
