package fr.imag.steamer.tsn.tsnrdf.beans;

import java.io.Serializable;

/**
 *
 * Defini une catégorie pour une réponse. 
 * 
 * Une catégorie est décrite par :
 * 
 * - le phonème: défini la catégorie
 * - un numéro: les catégories sont numérotées de 0 à n
 * - un effectif: le nombre de points d'enquête situés dans la catégorie.
 * 
 * @author Philippe GENOUD - Université Grenoble Alpes - Lab LIG-Steamer
 */
public class ResponseCategory implements Comparable<ResponseCategory> , Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private final String phoneme;
    private final int rank;
    private final int size;

    /**
     * 
     * @param phoneme le phonème définissant la catégorie
     * @param rank le rang de la catégorie. La catégorie dont l'effectif est le
     *             plus grand à comme rang 0, la suivante le rang 1, etc..
     * @param size l'effectif de la catégorie
     */
    public ResponseCategory(String phoneme, int rank, int size) {
        this.phoneme = phoneme;
        this.rank = rank;
        this.size = size;
    }

    public String getPhoneme() {
        return phoneme;
    }

    public int getRank() {
        return rank;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int compareTo(ResponseCategory other) {
        return this.rank - other.rank;
    }
    
}
