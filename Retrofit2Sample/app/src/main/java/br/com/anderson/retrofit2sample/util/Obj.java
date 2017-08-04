package br.com.anderson.retrofit2sample.util;

/**
 * Created by viniciusthiengo on 10/11/15.
 */
public class Obj {
    private String name;
    private String pathImg;
//    private String dataCriacao;


    public Obj() {}
    public Obj(String name, String pathImg) {
        this.name = name;
        this.pathImg = pathImg;
//        this.dataCriacao = dataCriacao;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

//    public String getDataCriacao() {
//        return dataCriacao;
//    }
//
//    public void setDataCriacao(String dataCriacao) {
//        this.dataCriacao = dataCriacao;
//    }
}
