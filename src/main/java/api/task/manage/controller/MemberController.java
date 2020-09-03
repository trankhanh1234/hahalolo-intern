package api.task.manage.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import api.task.manage.repository.MemberRepository;
import api.tast.manage.model.MemberModel;
import api.tast.manage.model.ServiceResult;
import api.tast.manage.model.ServiceResult.Status;

/*@Authur khanh tran
 * */
@RestController
public class MemberController {
	@Autowired
	private MemberRepository memberRepo;

	/*
	 * GET: /getAllMember
	 * 
	 * @Param output: fullname , phone number, start date, end date, member create.
	 */
	@GetMapping("/getAllMenber")
	public List<MemberModel> getAllMember() {
		List<MemberModel> listMember = memberRepo.findAll();
		List<MemberModel> ListResult = new ArrayList<MemberModel>();
		int len = listMember.size();
		for (int i = 0; i < len; i++) {
			ListResult.add(new MemberModel(listMember.get(i).getFullname(), listMember.get(i).getPhonenumber(),
					listMember.get(i).getMembercreate(), listMember.get(i).getCreatedate(),
					listMember.get(i).getCreatedate()));
		}
		return ListResult;
	}

	/*
	 * Get member by id. GET: /getAllMember/{id}
	 * 
	 * @Param output : All.
	 * 
	 */

	@GetMapping("/getById/{id}")
	public Optional<MemberModel> getById(@PathVariable String id) {
		return memberRepo.findById(id);
	}

	/*
	 * Create one Member
	 * 
	 * @Param : fullname , phonenumber, address, avatar, birtsday, member,
	 * membercreate
	 * 
	 */

	@PostMapping("/createMember")
	public ResponseEntity<ServiceResult> createMember(@RequestBody MemberModel model) {
		ServiceResult result = new ServiceResult();
		Date date = new Date();
		// check data
		if (checkModel(model) == true) {
			MemberModel member = new MemberModel();
			model.setCreatedate(date);
			model.setMember(0);
			member = memberRepo.insert(model);
			result.setData(member);
			result.setMessage("Tạo mới thành công thành viên " + model.getFullname());
			result.setStatus(Status.SUCCESS);
			return new ResponseEntity<ServiceResult>(result, HttpStatus.OK);
		}
		result.setMessage("Vui lòng nhập đủ thông tin");
		return new ResponseEntity<ServiceResult>(result, HttpStatus.BAD_REQUEST);
	}

	/*
	 * Update one Member Url : /update/{id}
	 * 
	 * @Param: fullname , phonenumber, address, avatar, birtsday, member,
	 * membercreate
	 * 
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<ServiceResult> updateMEmber(@PathVariable String id, @RequestBody MemberModel model) {
		Optional<MemberModel> member = memberRepo.findById(id);
		//Date timenow = new Date();
		ServiceResult result = new ServiceResult();
		// check data
		if (member.isPresent() && checkModel(model) == true) {
			MemberModel memerModel = member.get();
			memerModel.setFullname(model.getFullname());
			memerModel.setAddress(model.getAddress());
			memerModel.setAvatar(model.getAvatar());
			memerModel.setBirthday(model.getBirthday());
			memerModel.setPhonenumber(model.getPhonenumber());
			memerModel.setMembercreate(model.getMembercreate());
			if (after(model.getCreatedate()) == true) {
				memerModel.setMember(model.getMember());
			} else {
				memerModel.setMember(0);
			}
			result.setData(memberRepo.save(memerModel));
			result.setMessage("Cập nhập thành công");
			result.setStatus(Status.SUCCESS);
			return new ResponseEntity<ServiceResult>(result, HttpStatus.OK);
		}
		result.setMessage("Vui lòng kiểm tra lại");
		return new ResponseEntity<ServiceResult>(result, HttpStatus.BAD_REQUEST);
	}

	/*
	 * Delete one Member Url: /delete/{id}
	 * 
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable String id) {
		ServiceResult result = new ServiceResult();
		memberRepo.deleteById(id);
		result.setMessage("Xóa thành công");
		result.setStatus(Status.SUCCESS);
		return new ResponseEntity<ServiceResult>(result, HttpStatus.OK);
	}

	/*
	 * Check validate form body check fullname Validators.maxLength(30), isEmty ,
	 * Null , address check fullname Validators.maxLength(200), isEmty , Null phone
	 * number check fullname Validators.maxLength(11), isEmty , Null , Member
	 * 
	 */

	public boolean checkModel(MemberModel model) {
		if (model.getFullname() == null || (model.getFullname().length() <= 0 || model.getFullname().length() >= 31)) {
			return false;
		} else if (model.getAddress() == null
				|| (model.getAddress().length() <= 0 || model.getAddress().length() >= 201)) {
			return false;
		} else if (model.getPhonenumber() == null
				|| (model.getPhonenumber().length() > 12 || isValid(model.getPhonenumber()) == false)) {
			return false;
		} else if (model.getBirthday() == null) {
			return false;
		} else if (model.getMembercreate() == null || model.getMembercreate() == "") {
			return false;
		} else if (model.getMember() != 0 && model.getMember() != 1) {
			return false;
		}
		return true;
	}

	// Check validate Phone Number
	// regex "[0-9*#+() -]*"; là số 0-9 , - , * , + , () .
	private static boolean isValid(String s) {
		String regex = "[0-9*#+() -]*";// XXXXXXXXXX
		return s.matches(regex);
	}

	private boolean after(Date createdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 3);
		if (calendar.after(createdate) == false) {
			return false;
		}
		System.out.println(calendar.getTime());
		return true;
	}
}
