package ma.adria.batch.processor;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import ma.adria.batch.model.Flux;

public class ItemRetourprocessor implements ItemProcessor<Flux, Flux> {

//	@Value("#{batchParamProp['codePaysAssocie']}")
//	public String codePays;
//	@Value("#{batchParamProp['codeBanqueAssocie']}")
//	public String codeBanque;
//	@Autowired
//    private Flux flux;
	private static final Logger log = Logger.getLogger(ItemRetourprocessor.class);
	
	@Override
	public Flux process(Flux flux) throws Exception {
            
		/*
		 * return only the libelle which not contains "pr"
		 */
//		if(flux.getLIBELLE().contains("pr")){
//			//flux.setLIBELLE("Test");
//			return null;
//		}
		
		return flux;
	}
}
