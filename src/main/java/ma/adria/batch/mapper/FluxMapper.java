package ma.adria.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ma.adria.batch.model.Flux;
/**
 * 
 * @author bahadi on 03/04/2017
 * this class is responsible to do the mapping between the result and the row
 *
 */

public class FluxMapper implements RowMapper<Flux> {

	@Override
	public Flux mapRow(ResultSet rs, int i) throws SQLException {
		Flux flux=new Flux();
		
		
		//private String CODETRA;
		flux.setLIBELLE(rs.getString("LIBELLE"));
		flux.setCODE_ETAT(rs.getString("CODE_ETAT"));
		
		flux.setTYPE_CODE(rs.getString("TYPE_CODE"));
		flux.setCODE(rs.getString("CODE"));
		
		flux.setCODE_STATUT(rs.getString("CODE_STATUT"));
	
		flux.setDATE_TRT_ADRIA(rs.getDate("DATE_TRT_ADRIA"));
		flux.setID_LOT(rs.getLong("ID_LOT"));
		flux.setDATE_CHARGEMENT(rs.getDate("DATE_CHARGEMENT"));
		flux.setCODE_RETOUR(rs.getString("CODE_RETOUR"));
		flux.setMSG_RETOUR(rs.getString("MSG_RETOUR"));
		
		return flux;
	}

}
