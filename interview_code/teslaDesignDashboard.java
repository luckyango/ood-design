// export const equipment = [
//   { id: "EQ-001", name: "Oscilloscope Rigol DS1054Z", category: "Test",        checkedOutBy: null,              dueDate: null },
//   { id: "EQ-002", name: "Torque Wrench 3/8\"",        category: "Hand Tool",   checkedOutBy: "Maria Gonzalez",  dueDate: "2026-02-10" },
//   { id: "EQ-003", name: "Thermal Camera FLIR E8",     category: "Test",        checkedOutBy: "James Chen",      dueDate: "2026-02-08" },
//   { id: "EQ-004", name: "Calibration Kit Fluke 5522A",category: "Calibration", checkedOutBy: null,              dueDate: null },
//   { id: "EQ-005", name: "Multimeter Fluke 87V",       category: "Test",        checkedOutBy: "Priya Patel",     dueDate: "2026-02-12" },
//   { id: "EQ-006", name: "Soldering Station Hakko",    category: "Hand Tool",   checkedOutBy: null,              dueDate: null },
//   { id: "EQ-007", name: "LCR Meter Keysight E4980A",  category: "Test",        checkedOutBy: "James Chen",      dueDate: "2026-02-07" },
//   { id: "EQ-008", name: "Crimping Tool Molex",        category: "Hand Tool",   checkedOutBy: "Maria Gonzalez",  dueDate: "2026-02-11" },
//   { id: "EQ-009", name: "Power Supply Keithley 2200", category: "Test",        checkedOutBy: null,              dueDate: null },
//   { id: "EQ-010", name: "Torque Screwdriver Set",     category: "Hand Tool",   checkedOutBy: "Alex Kim",        dueDate: "2026-02-06" },
// ];

// eg. 1. James Chen
// eg. 2. James Chan


// 1.the purpose of the system
// contractors to check out equiments -> record the status of equipment
// 2.entities
// (1) db. where the data is from: we store the data in the local db -> MySQL
// (2) backend framework. springboot 
// (3) frontend: vue
// 3.data
// id, name , category, checkedOutBy, dueDate
//   equipmentStatus table: columns (int id, String/varchar name, enum category, varchar checkedOutBy, dueDate) 
// // 4. api
// (1) display - get /status/
// (2) modify - 
// class checkEquipmentStatus{
// 	class EquipmentStatus{
//   	int id; String name; Enum category; String checkedOutBy; String dueDate;
//   }
// 	public List<EquipmentStatus> getStatus(int pageSize, int offset){
//   	// 1. get from the database
//     String sql = "select from equipmentStatus limit ? offset ?";
    
//   }

// }