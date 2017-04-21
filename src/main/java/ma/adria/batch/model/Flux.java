package ma.adria.batch.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;
/**
 * 
 * @author bahadi on 03/04/2017
 *
 */
@Component("flux")
@XmlRootElement(name = "flux")
public class Flux implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TYPE_CODE;
	private String CODE;
	private String CODE_LANGUE;
	private String CLASS;

	
	//private String LIB_COURT;
	private String LIBELLE;
	private String CODE_STATUT;
	private long ID_LOT;
	private Date DATE_CHARGEMENT;
	private String CODE_ETAT;
	private String CODE_ETATToUpdate;
	
	private Date DATE_TRT_ADRIA;
	private String CODE_RETOUR;
	private String MSG_RETOUR;
	//private String CODETRA;
	
	/*
	 * getters and setters
	 */
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}
	public String getCODE_ETATToUpdate() {
		return CODE_ETATToUpdate;
	}
	public void setCODE_ETATToUpdate(String cODE_ETATToUpdate) {
		CODE_ETATToUpdate = cODE_ETATToUpdate;
	}
	public String getCODE_LANGUE() {
		return CODE_LANGUE;
	}
	public void setCODE_LANGUE(String cODE_LANGUE) {
		CODE_LANGUE = cODE_LANGUE;
	}
	public String getTYPE_CODE() {
		return TYPE_CODE;
	}
	public void setTYPE_CODE(String tYPE_CODE) {
		TYPE_CODE = tYPE_CODE;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	@XmlElement(name = "Libelle")
	public String getLIBELLE() {
		return LIBELLE;
	}
	public void setLIBELLE(String lIBELLE) {
		LIBELLE = lIBELLE;
	}
	public String getCODE_STATUT() {
		return CODE_STATUT;
	}
	public void setCODE_STATUT(String cODE_STATUT) {
		CODE_STATUT = cODE_STATUT;
	}
	
	public long getID_LOT() {
		return ID_LOT;
	}
	public void setID_LOT(long iD) {
		ID_LOT = iD;
	}
	public Date getDATE_CHARGEMENT() {
		return DATE_CHARGEMENT;
	}
	public void setDATE_CHARGEMENT(Date dATE_CHARGEMENT) {
		DATE_CHARGEMENT = dATE_CHARGEMENT;
	}
	@XmlElement(name = "CODE_ETAT")
	public String getCODE_ETAT() {
		return CODE_ETAT;
	}
	public void setCODE_ETAT(String cODE_ETAT) {
		CODE_ETAT = cODE_ETAT;
	}
	public Date getDATE_TRT_ADRIA() {
		return DATE_TRT_ADRIA;
	}
	public void setDATE_TRT_ADRIA(Date dATE_TRT_ADRIA) {
		DATE_TRT_ADRIA = dATE_TRT_ADRIA;
	}
	public String getCODE_RETOUR() {
		return CODE_RETOUR;
	}
	public void setCODE_RETOUR(String cODE_RETOUR) {
		CODE_RETOUR = cODE_RETOUR;
	}
	public String getMSG_RETOUR() {
		return MSG_RETOUR;
	}
	public void setMSG_RETOUR(String mSG_RETOUR) {
		MSG_RETOUR = mSG_RETOUR;
	}
	@Override
	public String toString() {
		return "Flux [TYPE_CODE=" + TYPE_CODE + ", CODE=" + CODE + ", LIBELLE=" + LIBELLE + ", CODE_STATUT="
				+ CODE_STATUT + ", ID_LOT=" + ID_LOT + ", DATE_CHARGEMENT=" + DATE_CHARGEMENT + ", CODE_ETAT="
				+ CODE_ETAT + ", DATE_TRT_ADRIA=" + DATE_TRT_ADRIA + ", CODE_RETOUR=" + CODE_RETOUR + ", MSG_RETOUR="
				+ MSG_RETOUR + "]";
	}
	
	
	
	
	
	
	
	

	
}
