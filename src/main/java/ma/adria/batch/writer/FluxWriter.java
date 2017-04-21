package ma.adria.batch.writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;


import ma.adria.batch.model.Flux;

//@Component("fluxWriter")
@Scope("singleton") 
public class FluxWriter implements ItemWriter<Flux> {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	DataSource dataSource;

	@Value("${ma.adria.insert}")
	String sql;
	
	@Override
	public void write(List<? extends Flux> items) throws Exception {
		for(Object flux : items){
			Flux f=(Flux) flux;
				Map<String, Object> Param = new HashMap<String, Object>();
					Param.put("codeEtat", f.getCODE_ETAT());
					Param.put("codeLibelle", f.getLIBELLE());
				getJdbcTemplate().update(sql, Param);
			}
		
	}
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate==null) {
			
			jdbcTemplate= new NamedParameterJdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}
}
