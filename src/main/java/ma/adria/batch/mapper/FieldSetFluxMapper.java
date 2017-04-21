package ma.adria.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import ma.adria.batch.model.Flux;

//@Component("bodyMapper")
public class FieldSetFluxMapper implements FieldSetMapper<Flux> {

	@Override
	public Flux mapFieldSet(FieldSet fieldSet) throws BindException {
		Flux flux=new Flux();
		flux.setCODE_ETAT(fieldSet.readString("CODE_ETAT"));
		flux.setLIBELLE(fieldSet.readString("LIBELLE"));
		return flux;
	}

}
