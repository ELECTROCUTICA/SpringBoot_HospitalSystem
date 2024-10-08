package com.HospitalSystem.Service;

import com.HospitalSystem.Entity.Page;
import com.HospitalSystem.Entity.Patient;
import com.HospitalSystem.Map.DoctorArrangementMap;
import com.HospitalSystem.Utils.PatientArrangementResponse;
import com.HospitalSystem.Utils.PatientRecordsResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PatientService {

    Map<String, Object> getServerTime();

    Map<String, Object> getRegistrationsToday(String dateParam, HttpServletRequest request);

    Map<String, Object> loginHandle(String id, String password, HttpServletRequest request);

    Map<String, Object> registerHandle(String id, String name, String sex, String birthdate, String password);

    PatientRecordsResponse getPatientRecords(String p, Patient patient);

    Map<String, Object> cancelRegistration(String id, Patient patient);

    Map<String, Object> editHandle(String id, String name, String sex, String birthdate, String password, HttpServletRequest request);

    PatientArrangementResponse getArrangement();

    ArrayList<DoctorArrangementMap> getDoctorsWorkAtDate(Integer dep_no, String date);

    /**  病人获取指定医师的简介
     * @param doctor_id   目标医师
     * @param date  目标时间
     * @return 指定医生的简介
     */
    String getDoctorDescription(String doctor_id, String date);

    /**  病人提交挂号申请
     * @param doctor_id    挂号的目标医师
     * @param date  预约就医日期
     * @param request request请求，传递病人token并解析成patient对象
     * @return 挂号结果信息，HashMap集合
     */
    Map<String, Object> registrationSubmit(String doctor_id, String date, HttpServletRequest request);

    int getCounts();
}
