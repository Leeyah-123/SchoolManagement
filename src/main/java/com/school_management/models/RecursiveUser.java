package com.school_management.models;

public class RecursiveUser {
        private int teacherID;
        private String firstName, lastName, email, gender, number, password, username, role, phoneNum;

        public RecursiveUser() {

        }

        public RecursiveUser(String firstName, String lastName, String email, String gender, String number, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.gender = gender;
            this.number = number;
            this.password = password;
        }

        public int getTeacherID() {
            return teacherID;
        }

        public void setTeacherID(int teacherID) {
            this.teacherID = teacherID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public String getNumber() {
            return number;
        }
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
