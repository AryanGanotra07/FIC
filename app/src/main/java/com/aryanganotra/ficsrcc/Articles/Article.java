package com.aryanganotra.ficsrcc.Articles;

import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article implements Parcelable {

    public String link;
    public String date;
   public Title title;
   public Content content;

   public int featured_media;
   public int id;
   public Embedded _embedded;






    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getFeatured_media() {
        return featured_media;
    }

    public void setFeatured_media(int featured_media) {
        this.featured_media = featured_media;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
    }

    public Article(Title title, String date, String link, Content content, int featured_media, int id, Embedded _embedded) {

        this.date=date;
        this.link=link;
        this.title=title;
        this.content=content;
        this.featured_media=featured_media;

        this.id=id;
        this._embedded=_embedded;

    }



    public String getDate() {
        return date;
    }




    public void setDate(String date) {
        this.date = date;
    }


    public static class Embedded implements Parcelable{
        @SerializedName("wp:featuredmedia")
        private List<WPFeaturedMedia> wpfeaturemedia;

        protected Embedded(Parcel in) {
            wpfeaturemedia = in.createTypedArrayList(WPFeaturedMedia.CREATOR);

        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(wpfeaturemedia);


        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Embedded> CREATOR = new Creator<Embedded>() {
            @Override
            public Embedded createFromParcel(Parcel in) {
                return new Embedded(in);
            }

            @Override
            public Embedded[] newArray(int size) {
                return new Embedded[size];
            }
        };

        public List<WPFeaturedMedia> getWpfeaturemedia() {
            return wpfeaturemedia;
        }

        public void setWpfeaturemedia(List<WPFeaturedMedia> wpfeaturemedia) {
            this.wpfeaturemedia = wpfeaturemedia;
        }

        public Embedded(List<WPFeaturedMedia> wpFeaturedMedia){
            this.wpfeaturemedia=wpFeaturedMedia;
        }

        public static class WPFeaturedMedia implements Parcelable{
            private String source_url;

            protected WPFeaturedMedia(Parcel in) {
                source_url = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(source_url);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<WPFeaturedMedia> CREATOR = new Creator<WPFeaturedMedia>() {
                @Override
                public WPFeaturedMedia createFromParcel(Parcel in) {
                    return new WPFeaturedMedia(in);
                }

                @Override
                public WPFeaturedMedia[] newArray(int size) {
                    return new WPFeaturedMedia[size];
                }
            };

            public String getSource_url() {
                return source_url;
            }

            public void setSource_url(String source_url) {
                this.source_url = source_url;
            }

            public WPFeaturedMedia(String source_url){
                this.source_url=source_url;
            }
        }
    }


    public static class Title implements Parcelable{

        public String rendered;

        protected Title(Parcel in) {
            rendered = in.readString();
        }

        public static final Creator<Title> CREATOR = new Creator<Title>() {
            @Override
            public Title createFromParcel(Parcel in) {
                return new Title(in);
            }

            @Override
            public Title[] newArray(int size) {
                return new Title[size];
            }
        };

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public Title(String rendered){
            this.rendered=rendered;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(rendered);
        }
    }

    public static class Content implements Parcelable{
        public String rendered;

        public Content(String rendered){
            this.rendered=rendered;
        }


        protected Content(Parcel in) {
            rendered = in.readString();
        }

        @SuppressWarnings("unused")

        public static   final Creator<Content> CREATOR = new Creator<Content>() {
            @Override
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            @Override
            public Content[] newArray(int size) {
                return new Content[size];
            }
        };

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(rendered);
        }
    }

    protected Article(Parcel in){
        link=in.readString();
        date=in.readString();
        title=(Title)in.readValue(Title.class.getClassLoader());
        content=(Content)in.readValue(Content.class.getClassLoader());

        featured_media=in.readInt();
        id=in.readInt();
        _embedded=(Embedded)in.readValue(Embedded.class.getClassLoader());

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeString(date);
        dest.writeValue(title);
        dest.writeValue(content);
        dest.writeInt(featured_media);
        dest.writeInt(id);
        dest.writeValue(_embedded);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Article> CREATOR=new Parcelable.Creator<Article>(){


        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}
