import java.util.*;
public class MeetingRoomScheduler {
    // requirement
    //     Multiple meeting rooms.
    // A meeting has a start and end time.
    // Schedule it into any available room.
    // No overlapping meetings in the same room.
    // Support booking and cancellation.
    
    // entity
    // 1.meeting room management system
    // receive booking/ cancelation requirement
    // find a meeting room based on the meeting time & other stratery (smallest, nearest)
    // 2. meeting
    // id, start time,end time
    // 3. room
    // id, list<meeting> 
    // method: isAvailable(start time, end time), addMeeting, removeMeeting
    static class Meeting{
        String id; int startTime; int endTime;
        public Meeting(String id, int startTime, int endTime){
            this.id=id;this.startTime = startTime; this.endTime = endTime;
        }
    }
    static class Room{
        String id; List<Meeting> meetings;
        public Room(String id){
            this.id = id;
            meetings = new ArrayList<>();
        }
        public boolean isAvailable(int startTime, int endTime){
            for(Meeting cur: meetings){
                // if there is no overlap -> meeting.end <= cur.start or cur.start >= meeting.end
                if(!(endTime <= cur.startTime || startTime >= cur.endTime)) return false;
            }
            return true;
        }
        public void addMeeting(Meeting meeting){
            meetings.add(meeting);
        }
        public void removeMeeting(String meetingId){
            Iterator<Meeting> it = meetings.iterator();
            while(it.hasNext()){
                if(meetingId.equals(it.next().id)){
                    it.remove(); return;
                }
            }
        }
    }
    interface RoomSelectStrategy{
        Room selectRoom(List<Room> rooms, Meeting meeting);
    }
    public static class FirstAvailableStrategy implements RoomSelectStrategy{
        @Override
        public Room selectRoom(List<Room> rooms, Meeting meeting){
            for(Room room: rooms){
                if(room.isAvailable(meeting.startTime, meeting.endTime)){
                    return room;
                }
            }
            return null;
        }
    }
    public static class MeetingRoomManagement {
        List<Room> rooms = new ArrayList<>();
        // cancel -> use meeting id to find the booked room
        Map<String,Room> meetingToRoom = new HashMap<>();
        RoomSelectStrategy roomSelectStrategy;
        public MeetingRoomManagement(List<Room> rooms, RoomSelectStrategy roomSelectStrategy){
            this.rooms=rooms;
            this.roomSelectStrategy = roomSelectStrategy;
        }
        public boolean scheduleMeeting(Meeting meeting){
            if(meeting==null || meetingToRoom.containsKey(meeting.id) || meeting.startTime >= meeting.endTime) return false;
            // for(Room room: rooms){
            //     if(room.isAvailable(meeting.startTime, meeting.endTime)){
            //         room.addMeeting(meeting);
            //         meetingToRoom.put(meeting.id, room);
            //         return true;
            //     }
            // }
            Room room = roomSelectStrategy.selectRoom(rooms, meeting);
            if(room==null) return false;
            meetingToRoom.put(meeting.id, room);
            room.addMeeting(meeting);
            return true;
        }
        public boolean cancelMeeting(String meetingId){
            Room room = meetingToRoom.get(meetingId);
            if(room==null) return false;
            room.removeMeeting(meetingId);
            meetingToRoom.remove(meetingId);
            return true;
        }
        
    }
}
