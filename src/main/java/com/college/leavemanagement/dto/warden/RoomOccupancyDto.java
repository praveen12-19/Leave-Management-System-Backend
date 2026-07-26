package com.college.leavemanagement.dto.warden;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.enums.HostelBlockName;

import java.util.List;

public class RoomOccupancyDto {
    private HostelBlockName hostelBlock;
    private String roomNumber;
    private int totalStudents;
    private int awayStudentCount;
    private List<StudentProfileDto> awayStudents;
    private List<StudentProfileDto> roomRoster;

    public RoomOccupancyDto() {}

    public RoomOccupancyDto(HostelBlockName hostelBlock, String roomNumber, int totalStudents, int awayStudentCount,
                            List<StudentProfileDto> awayStudents, List<StudentProfileDto> roomRoster) {
        this.hostelBlock = hostelBlock;
        this.roomNumber = roomNumber;
        this.totalStudents = totalStudents;
        this.awayStudentCount = awayStudentCount;
        this.awayStudents = awayStudents;
        this.roomRoster = roomRoster;
    }

    public static RoomOccupancyDtoBuilder builder() {
        return new RoomOccupancyDtoBuilder();
    }

    public HostelBlockName getHostelBlock() { return hostelBlock; }
    public void setHostelBlock(HostelBlockName hostelBlock) { this.hostelBlock = hostelBlock; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }

    public int getAwayStudentCount() { return awayStudentCount; }
    public void setAwayStudentCount(int awayStudentCount) { this.awayStudentCount = awayStudentCount; }

    public List<StudentProfileDto> getAwayStudents() { return awayStudents; }
    public void setAwayStudents(List<StudentProfileDto> awayStudents) { this.awayStudents = awayStudents; }

    public List<StudentProfileDto> getRoomRoster() { return roomRoster; }
    public void setRoomRoster(List<StudentProfileDto> roomRoster) { this.roomRoster = roomRoster; }

    public static class RoomOccupancyDtoBuilder {
        private HostelBlockName hostelBlock;
        private String roomNumber;
        private int totalStudents;
        private int awayStudentCount;
        private List<StudentProfileDto> awayStudents;
        private List<StudentProfileDto> roomRoster;

        RoomOccupancyDtoBuilder() {}

        public RoomOccupancyDtoBuilder hostelBlock(HostelBlockName hostelBlock) { this.hostelBlock = hostelBlock; return this; }
        public RoomOccupancyDtoBuilder roomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
        public RoomOccupancyDtoBuilder totalStudents(int totalStudents) { this.totalStudents = totalStudents; return this; }
        public RoomOccupancyDtoBuilder awayStudentCount(int awayStudentCount) { this.awayStudentCount = awayStudentCount; return this; }
        public RoomOccupancyDtoBuilder awayStudents(List<StudentProfileDto> awayStudents) { this.awayStudents = awayStudents; return this; }
        public RoomOccupancyDtoBuilder roomRoster(List<StudentProfileDto> roomRoster) { this.roomRoster = roomRoster; return this; }

        public RoomOccupancyDto build() {
            return new RoomOccupancyDto(hostelBlock, roomNumber, totalStudents, awayStudentCount, awayStudents, roomRoster);
        }
    }
}
