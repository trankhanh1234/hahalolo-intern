package api.tast.manage.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "menber")
public class MemberModel {
	@Id
	private String _id;
	private String fullname;
	private String address;
	private String phonenumber;
	private Date birthday;
	private String avatar;
	private int member;
	private Date createdate;
	private String membercreate;
	private Date Startdate;
	private Date enddate;
	public MemberModel(String fullname, String phonenumber, String membercreate, Date startdate, Date enddate) {
		this.fullname = fullname;
		this.phonenumber = phonenumber;
		this.membercreate = membercreate;
		Startdate = startdate;
		this.enddate = enddate;
	}
	public MemberModel() {
		super();
	}
	
	

	

}