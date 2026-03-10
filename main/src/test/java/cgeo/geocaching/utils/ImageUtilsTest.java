package cgeo.geocaching.utils;

import org.junit.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ImageUtilsTest {

    @Test
    public void fixDropboxImageUrl_oldFormatWithDl0() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/s/abc123/photo.jpg?dl=0"))
                .isEqualTo("https://www.dropbox.com/s/abc123/photo.jpg?dl=1");
    }

    @Test
    public void fixDropboxImageUrl_oldFormatWithDl1() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/s/abc123/photo.jpg?dl=1"))
                .isEqualTo("https://www.dropbox.com/s/abc123/photo.jpg?dl=1");
    }

    @Test
    public void fixDropboxImageUrl_oldFormatWithoutDlParam() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/s/abc123/photo.jpg"))
                .isEqualTo("https://www.dropbox.com/s/abc123/photo.jpg?dl=1");
    }

    @Test
    public void fixDropboxImageUrl_newSclFormatWithDl0() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz&dl=0"))
                .isEqualTo("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz&dl=1");
    }

    @Test
    public void fixDropboxImageUrl_newSclFormatWithDl1() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz&dl=1"))
                .isEqualTo("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz&dl=1");
    }

    @Test
    public void fixDropboxImageUrl_newSclFormatWithoutDlParam() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz"))
                .isEqualTo("https://www.dropbox.com/scl/fi/abc123/photo.jpg?rlkey=xyz&dl=1");
    }

    @Test
    public void fixDropboxImageUrl_dlDropboxusercontent_unchanged() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://dl.dropboxusercontent.com/s/abc123/photo.jpg"))
                .isEqualTo("https://dl.dropboxusercontent.com/s/abc123/photo.jpg");
    }

    @Test
    public void fixDropboxImageUrl_dlDropboxCom_unchanged() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://dl.dropbox.com/s/abc123/photo.jpg?dl=1"))
                .isEqualTo("https://dl.dropbox.com/s/abc123/photo.jpg?dl=1");
    }

    @Test
    public void fixDropboxImageUrl_nonDropboxUrl_unchanged() {
        assertThat(ImageUtils.fixDropboxImageUrl("https://img.geocaching.com/cache/abc123.jpg"))
                .isEqualTo("https://img.geocaching.com/cache/abc123.jpg");
    }

    @Test
    public void fixDropboxImageUrl_dlParamInFilenameNotAffected() {
        // dl=0 in a filename path segment should not be mistakenly replaced
        assertThat(ImageUtils.fixDropboxImageUrl("https://www.dropbox.com/s/abc123/photo_dl=0.jpg?rlkey=xyz"))
                .isEqualTo("https://www.dropbox.com/s/abc123/photo_dl=0.jpg?rlkey=xyz&dl=1");
    }

}
