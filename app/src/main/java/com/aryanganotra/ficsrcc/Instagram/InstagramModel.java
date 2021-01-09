package com.aryanganotra.ficsrcc.Instagram;

import java.util.List;

public class InstagramModel {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public InstagramModel(List<Data> data) {

        this.data = data;
    }


    public class Data {

        private Images images;
        private List<CarouselMedia> carousel_media;
        private String type;


        public List<CarouselMedia> getCarousel_media() {
            return carousel_media;
        }

        public void setCarousel_media(List<CarouselMedia> carousel_media) {
            this.carousel_media = carousel_media;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Data(Images images, List<CarouselMedia> carousel_media, String type) {
            this.images = images;
            this.carousel_media = carousel_media;
            this.type=type;

        }


        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;

        }



        public class Images {
            private StandardResolution standard_resolution;

            public StandardResolution getStandard_resolution() {
                return standard_resolution;
            }

            public void setStandard_resolution(StandardResolution standard_resolution) {
                this.standard_resolution = standard_resolution;
            }

            public Images(StandardResolution standard_resolution) {
                this.standard_resolution = standard_resolution;

            }

            public class StandardResolution {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public StandardResolution(String url) {
                    this.url = url;


                }

            }

        }


        public class CarouselMedia {

            private Images images;

            public Images getImages() {
                return images;
            }

            public void setImages(Images images) {
                this.images = images;
            }

            public CarouselMedia(Images images) {
                this.images = images;


            }


        }


    }

}





