package com.example.erlanggarizky.forumkita;

public class ModelUser {

    //firebase
    String name, email, image, jurusan, universitas, search, uid;

    public ModelUser() {

    }

    public ModelUser(String name, String email, String image, String jurusan, String universitas, String search, String uid) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.jurusan = jurusan;
        this.universitas = universitas;
        this.search = search;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getUniversitas() {
        return universitas;
    }

    public void setUniversitas(String universitas) {
        this.universitas = universitas;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
