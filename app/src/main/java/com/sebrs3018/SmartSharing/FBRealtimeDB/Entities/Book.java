package com.sebrs3018.SmartSharing.FBRealtimeDB.Entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Book implements Parcelable {

    private long idlogicalClock;
    private String ISBN;
    private String titolo;
    private String autore;
    private String editore;
    private String dataPubblicazione;
    private String nroPagine;
    private String descrizione;
    private String urlImage;
    private String lender;

    public Book(){
        /* Default constructor required by Firebase */
    }

    /**
     * @param _ISBN book to search in the DB
     * @param _lender the guy who lent the book
    * */
    public Book( String _ISBN, String _titolo, String _autore, String _editore, String _dataPubblicazione, String _nroPagine, String _descrizione, String _urlImage, String _lender ){
        ISBN = _ISBN;
        titolo = _titolo;
        autore = _autore;
        editore = _editore;
        dataPubblicazione = _dataPubblicazione;
        nroPagine = _nroPagine;
        descrizione = _descrizione;
        urlImage = _urlImage;
        lender = _lender;
    }

    protected Book(Parcel in) {
        ISBN = in.readString();
        titolo = in.readString();
        autore = in.readString();
        editore = in.readString();
        dataPubblicazione = in.readString();
        nroPagine = in.readString();
        descrizione = in.readString();
        urlImage = in.readString();
        lender = in.readString();
    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(String dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public String getNroPagine() {
        return nroPagine;
    }

    public void setNroPagine(String nroPagine) {
        this.nroPagine = nroPagine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ISBN);
        dest.writeString(titolo);
        dest.writeString(autore);
        dest.writeString(editore);
        dest.writeString(dataPubblicazione);
        dest.writeString(nroPagine);
        dest.writeString(descrizione);
        dest.writeString(urlImage);
        dest.writeString(lender);
    }

    public long getIDlogicalClock() {
        return idlogicalClock;
    }

    public void setIDlogicalClock(long IDlogicalClock) {
        this.idlogicalClock = IDlogicalClock;
    }

}
