package api.task.manage.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import api.tast.manage.model.MemberModel;


/*@Authur Khanh tran
 * 
 * */
public interface MemberRepository extends MongoRepository<MemberModel, String> {
	
	@Query("{'_id' :?0}")
	MemberModel findBy_Id(String id) ;
	
	
	@Query("{},{fullname: 1 , address : 1}")
	List<MemberModel>  findAllMember();
}
