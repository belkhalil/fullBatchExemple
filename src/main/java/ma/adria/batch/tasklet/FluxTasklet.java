package ma.adria.batch.tasklet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ma.adria.batch.mapper.FluxMapper;
import ma.adria.batch.model.Flux;
import ma.adria.batch.utils.ResponseMessage;
import ma.adria.batch.utils.Utilities;

/**
 * @author bahadi
 *
 */
@SuppressWarnings({"deprecation" , "unused"})

public class FluxTasklet implements Tasklet, InitializingBean {
	private static final String PATTERN = "_yyyy-dd-MM_hh-mm-ss";


	private static final Logger LOGGER = LoggerFactory.getLogger(FluxTasklet.class);


	private DataSource dataSource;
   @Value("#{batchParamProp['CODE_ETAT']}")
	private String CODE_ETAT;
   @Value("#{batchParamProp['CODE_LANGUE']}")
	private String CODE_LANGUE;
    
	private String CLASS;
	private String CODE_RETOUR;
	private String filePath;               
	private String selectFlux;
	private String insertFlux;
	private String updateFlux;
	private String selectMax;
	private String MSG_RETOUR;
	private String CODE_ETATToUpdate;
    private String insertExelLog;
    private String NOM_TABLE;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("Début traitement");
		Connection conn = getDataSource().getConnection();
		/*
		 * Retrieve a flux  from db with a parameter CODETRA
		 */
		
		NamedParameterJdbcTemplate myJDBC = new NamedParameterJdbcTemplate(getDataSource());
		ResponseMessage responseMessage=new ResponseMessage();
		// Assume a valid connection object conn
		conn.setAutoCommit(false);
		final long startTime = System.currentTimeMillis();
		Long durationExcution =0L;
		List<Flux> flux=new ArrayList<Flux>();
		int nbrLignetraite=0;
		Long CodeReference=chunkContext.getStepContext().getStepExecution()
			      .getJobParameters().getLong("CODE_REFERENCE");
		//System.out.println(CodeReference);
		
		//System.exit(0);
		//System.out.println("start time"+startTime);
		try {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("CODE_ETAT", CODE_ETAT);
			 flux = myJDBC.query(selectFlux, parameters, new FluxMapper());
			//System.exit(0);
			if (flux.size() != 0) {
				long ID=myJDBC.queryForLong(selectMax, parameters);
				System.out.println(ID);
				System.out.println(flux.toString());
				String dateWriting=Utilities.formatDate(new Date(), PATTERN);
				//Writing in file
				filePath +=dateWriting;
				filePath +=".txt";
				File file = new File(filePath);
				FileOutputStream fos=new FileOutputStream(file);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		        StringBuilder text = new StringBuilder("");
		        text.append("Code: "+"CODE_ETAT: "+"CODETRA: "+"LIB_COURT: "+"LIBELLE: \n");
				
				for(Flux f:flux){
					 
				//Increment ID  inserted
				ID++;
				//building a flux object to insert
				Flux fluxToInsert=new Flux();
				fluxToInsert.setCODE(f.getCODE());
				fluxToInsert.setCODE_ETAT(f.getCODE_ETAT());
				//fluxToInsert.setCODETRA(f.getCODETRA());
				fluxToInsert.setLIBELLE(f.getLIBELLE());
				fluxToInsert.setTYPE_CODE(f.getTYPE_CODE());
				fluxToInsert.setCODE_LANGUE(CODE_LANGUE);
				fluxToInsert.setCLASS(this.getCLASS());
				fluxToInsert.setID_LOT(ID);
				/**Insertion of flux to db
				 * we can use MapSqlParameterSource parametersFlux = new MapSqlParameterSource() 
				 * we will work by BeanPropertySqlParameterSource 				 */
				//add object to parameter and execute request
				BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(fluxToInsert);
			    int res=myJDBC.update(insertFlux, beanParamSource);
			    //Increment count of line inserted
			     nbrLignetraite++;
			     //Insertion in file
//			     text.append(Utilities.completeValue(f.getCODE(), " - ",8))
//			        .append(Utilities.completeValue(f.getCODE_ETAT(), " ", 20))
//			        .append(Utilities.completeValue(f.getLIBELLE(), " ", 15));
//			        if(f!=null)
//			        text.append("\n");
//			  
			 System.out.println("requete : "+insertFlux);
			 if (res == 1) {
				    responseMessage.setCODE_RETOUR("1");
				    responseMessage.setMSG_RETOUR("Traitement avec succès");
					System.out.println("Commit Success");
					//building a flux object to update
					Flux fluxToUpdate=new Flux();
					fluxToUpdate.setCODE_RETOUR(responseMessage.getCODE_RETOUR());
					fluxToUpdate.setCODE_ETAT(CODE_ETAT);
					fluxToUpdate.setMSG_RETOUR(responseMessage.getMSG_RETOUR());
					fluxToUpdate.setDATE_TRT_ADRIA(new Date());
					fluxToUpdate.setCODE_ETATToUpdate(this.getCODE_ETATToUpdate());
					//fluxToUpdate.setCODE_ETAT("TRAI");
					//Map<String, Integer> param=new HashMap<String, Integer>();
					//param.put("CODE_RETOUR", 1);
					BeanPropertySqlParameterSource beanParamSourceUpdate = new BeanPropertySqlParameterSource(fluxToUpdate);
					myJDBC.update(updateFlux, beanParamSourceUpdate);
					System.out.println("Update Success");
					
					
				} else if (res != 1) {
					 responseMessage.setCODE_RETOUR("0");
					 responseMessage.setMSG_RETOUR("Traitement echoué");
					conn.rollback();
					System.out.println("Erreur insertion");
				}
				}
				 bw.write(text.toString());
			     bw.newLine();
			     bw.close();
//			  // depot du ficher dans le serveur servcaris
//			     String server = "192.168.1.130";
//			     int port = 21;
//			     String user = "serveur";
//			     String pass = "hE86ds2h";
//			     FTPClient ftpClient = new FTPClient();
//			     String remoteFilePath = "/home/serveur/BatchReclamations/flux.csv";
//			     try {
//			     // connect and login to the server
//			     ftpClient.connect(server, port);
//			     Boolean logged = ftpClient.login(user, pass);
//			     if (logged == true) {
//			     ftpClient.enterLocalPassiveMode();
//			     System.out.println("Connected");
//			     boolean uploaded =uploadSingleFile(ftpClient, filePath, remoteFilePath);
//			     if(uploaded == true)
//			     {
//			     System.out.println("file uploaded");
//			     }else {
//			     System.out.println("sorry there is a problem in uploading");
//			     }
//			     boolean logout = ftpClient.logout();
//			     ftpClient.disconnect();
//			     }
//			     } catch (IOException ex) {
//			     ex.printStackTrace();
//			     }
			 
				//INSERTION IN EXEC-LOG TABLE
				MapSqlParameterSource parametersInsert = new MapSqlParameterSource();
				/*
				 * defining parameters
				 */
				parametersInsert.addValue("REFERENCE_CHARGEMENT", CodeReference);
				Date DATE_TRAITEMENT=new Date();
				String NOM_TRT_BATCH=NOM_TABLE.substring(5, 10).toUpperCase();
				parametersInsert.addValue("NOM_TRT_BATCH", NOM_TRT_BATCH);
				parametersInsert.addValue("DATE_TRAITEMENT", DATE_TRAITEMENT);
				parametersInsert.addValue("NOMBRE_LIGNES_ATRA", flux.size());
				parametersInsert.addValue("NOMBRE_LIGNES_TRAI",nbrLignetraite);
				int NOMBRE_LIGNES_ERRE=flux.size()-nbrLignetraite;
				parametersInsert.addValue("NOMBRE_LIGNES_ERRE", NOMBRE_LIGNES_ERRE);
				parametersInsert.addValue("DATE_FIN_TRAITEMENT", DATE_TRAITEMENT);
			
				parametersInsert.addValue("NOM_TABLE", NOM_TABLE);
				parametersInsert.addValue("CODE_RETOUR", responseMessage.getCODE_RETOUR());
				parametersInsert.addValue("MESSAGE_RETOUR", responseMessage.getMSG_RETOUR());
				final long endTime = System.currentTimeMillis();
				durationExcution=calculateDuration(endTime, startTime);
				parametersInsert.addValue("DURRE_EXECUTION", durationExcution);
				//executing request
				
				
				int result=myJDBC.update(insertExelLog, parametersInsert);
				System.out.println("-----INSERTION IN EXEC-LOG TABLE");
				System.out.println("Le nombre de ligne à traités: "+flux.size());
				System.out.println("Le nombre de ligne  traités: "+nbrLignetraite);
				conn.commit();
				
			}
			
			else {
				LOGGER.info("le code langue{}",CODE_LANGUE);
				LOGGER.info("No result found withe this code: "+CODE_ETAT );
			}
            
//		} catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
			
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		} catch (IndexOutOfBoundsException e) {
			conn.rollback();
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}catch (Exception e) {
			conn.rollback();
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}finally{
			System.out.println("Finished ---" + "FluxTaskletJob" + " in: "
					+durationExcution + "MILLISECONDS");
			
		}
		return 
			RepeatStatus.FINISHED;
		
	}
	private static Long calculateDuration(long endTime, long starttime) {
//		final long duration = endTime - startTime;
//		StringBuilder sb = new StringBuilder();
//		sb.append(TimeUnit.MILLISECONDS.toHours(duration));
//		sb.append(" h ");
//		sb.append(TimeUnit.MILLISECONDS.toMinutes(duration)
//				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
//						.toHours(duration)));
//		sb.append(" min ");
//		sb.append(TimeUnit.MILLISECONDS.toSeconds(duration)
//				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
//						.toMillis(duration)));
//		sb.append(" sec ");
		Long difference=endTime-starttime;
		return difference;
	}
	/*
	 * getters and setters
	 */
	public String getNOM_TABLE() {
		return NOM_TABLE;
	}
	public void setNOM_TABLE(String nOM_TABLE) {
		NOM_TABLE = nOM_TABLE;
	}
	public String getCODE_ETATToUpdate() {
		return CODE_ETATToUpdate;
	}
	public void setCODE_ETATToUpdate(String cODE_ETATToUpdate) {
		CODE_ETATToUpdate = cODE_ETATToUpdate;
	}
	
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public String getUpdateFlux() {
		return updateFlux;
	}
	public void setUpdateFlux(String updateFlux) {
		this.updateFlux = updateFlux;
	}
	public String getSelectFlux() {
		return selectFlux;
	}

	public void setSelectFlux(String selectFlux) {
		this.selectFlux = selectFlux;
	}

	public String getInsertFlux() {
		return insertFlux;
	}

	public void setInsertFlux(String insertFlux) {
		this.insertFlux = insertFlux;
	}

	
//	public String getCODE_ETAT() {
//		return CODE_ETAT;
//	}
//
//	public void setCODE_ETAT(String cODE_ETAT) {
//		CODE_ETAT = cODE_ETAT;
//	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

//	public String getCODE_LANGUE() {
//		return CODE_LANGUE;
//	}
//
//	public void setCODE_LANGUE(String cODE_LANGUE) {
//		CODE_LANGUE = cODE_LANGUE;
//	}
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
	public String getInsertExelLog() {
		return insertExelLog;
	}
	public void setInsertExelLog(String insertExelLog) {
		this.insertExelLog = insertExelLog;
	}
	

	public String getSelectMax() {
		return selectMax;
	}
	public void setSelectMax(String selectMax) {
		this.selectMax = selectMax;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean uploadSingleFile(FTPClient ftpClient,String localFilePath, String remoteFilePath) throws IOException {
			File localFile = new File(localFilePath);

			InputStream inputStream = new FileInputStream(localFile);
			
			try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return ftpClient.storeFile(remoteFilePath, inputStream);
			} finally {
			inputStream.close();
			}
			}

}