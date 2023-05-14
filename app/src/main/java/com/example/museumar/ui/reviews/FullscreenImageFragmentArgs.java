package com.example.museumar.ui.reviews;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavArgs;

public class FullscreenImageFragmentArgs implements NavArgs {
        private final Bundle bundle;

        public FullscreenImageFragmentArgs(@NonNull Bundle bundle) {
                this.bundle = bundle;
        }

        @NonNull
        public static FullscreenImageFragmentArgs fromBundle(@NonNull Bundle bundle) {
                return new FullscreenImageFragmentArgs(bundle);
        }

        @NonNull
        public String getImageUrl() {
                if (bundle.containsKey("imageUrl")) {
                        return bundle.getString("imageUrl");
                } else {
                        throw new IllegalArgumentException("Required argument 'imageUrl' is missing and does not have a default value.");
                }
        }

        @NonNull
        public Bundle toBundle() {
                return bundle;
        }

        public static class Builder {
                private final Bundle bundle;

                public Builder(@NonNull String imageUrl) {
                        bundle = new Bundle();
                        bundle.putString("imageUrl", imageUrl);
                }

                @NonNull
                public FullscreenImageFragmentArgs build() {
                        return new FullscreenImageFragmentArgs(bundle);
                }
        }
}
