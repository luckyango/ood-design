import java.util.*;
class MessageService {
    private UserService userService;
    private CompanyService companyService;
    private MessageCountService messageCountService;
    private SendService sendService;
    public MessageService(UserService userService, CompanyService companyService, MessageCountService messageCountService, SendService sendService) {
        this.userService = userService;
        this.companyService = companyService;
        this.messageCountService = messageCountService;
        this.sendService = sendService;
    }
    public boolean sendMessage(String userId, String messageType) {
      System.err.println("in sendmessage ");
        // check user exists
        // messageCountService.incrementCount(userId);
        if (canSendMessage(userId, messageType)){
            messageCountService.incrementCount(userId);
            System.err.println("finish incrementCount ");
            sendService.sendMessage(userService.getUserById(userId), messageType);
            return true;
        } 
        // System.err.println("cant send ");
        return false;
    }
    private boolean canSendMessage(String userId, String messageType) {
      User user = userService.getUserById(userId);
      if(user == null) return false;
      if (user.getSubscriptionStatus().equals("not_active")) {
          return false;
      }
      int count = messageCountService.getMessageCount(userId);
      if (count >= 10) {
          return false;
      }
      Set<String> companyIds = user.getCompanyId();
      Set<String> activeTypes = new HashSet<>();
      for(String companyId: companyIds){
          Company company = companyService.getCompanyById(companyId);
          for(String type: company.getActiveMessageTypes()){
            System.err.println(type);
            activeTypes.add(type);
          }
      }      
      for(String act: activeTypes) System.err.println(act);
      if (!activeTypes.contains(messageType)) {
          return false;
      }
      return true;
  }
}
class Company {
    private String id;
    private String name;
    private List<String> activeMessageTypes;
    public Company(String id, String name, List<String> activeMessageTypes) {
        this.id = id;
        this.name = name;
        this.activeMessageTypes = activeMessageTypes;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getActiveMessageTypes() { return activeMessageTypes; }
}
class User {
    private String id;
    private String subscriptionStatus;
    // private String companyId;
    private Set<String> companyIds = new HashSet<>();
    public User(String id, String subscriptionStatus, String companyId) {
        this.id = id;
        this.subscriptionStatus = subscriptionStatus;
        this.companyIds.add(companyId);

    }
    public String getId() { return id; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    // public String getCompanyId() { return companyId; }
    public Set<String> getCompanyId() { return companyIds; }
}
/**
 * External service that sends messages to users
 * In production, this would make calls to send service API
 */
class SendService {
    public SendService() {}
    public void sendMessage(User user, String messageType) {
        System.out.printf("%s message sent to user %s%n", messageType, user.getId());
    }
}
/**
 * External service that fetches user data
 * In production, this would make calls to user service API which uses a database to store users
 */
class UserService {
    private Map<String, User> userDatabase;
    public UserService() {
        userDatabase = new HashMap<>();
    }
    public void addUser(User user) {
        userDatabase.put(user.getId(), user);
    }
    public User getUserById(String userId) {
        var user = userDatabase.get(userId);
        return user;
    }
}
/**
 * External service that fetches company settings
 * In production, this would make calls to company service API which uses a database to store companies
 */
class CompanyService {
    private Map<String, Company> companyDatabase;
    public CompanyService() {
        companyDatabase = new HashMap<>();
    }
    public void addCompany(Company company) {
        companyDatabase.put(company.getId(), company);
    }
    public Company getCompanyById(String companyId) {
        return companyDatabase.get(companyId);
    }
}
/**
 * External service that tracks message counts per user
 * In production, this would make calls to message count service API
 */
class MessageCountService {
    private Map<String, Integer> messageCounts;
    public MessageCountService() {
        messageCounts = new HashMap<>();
    }
    public int getMessageCount(String userId) {
        if(!messageCounts.containsKey(userId)) messageCounts.put(userId,0);
        return messageCounts.get(userId);
    }
    public void incrementCount(String userId) {
        int currentCount = messageCounts.getOrDefault(userId, 0);
        messageCounts.put(userId, currentCount + 1);
        
    }
}
class Solution {
  public static void main(String[] args) {
   // Setup mock services
    UserService userService = new UserService();
    CompanyService companyService = new CompanyService();
    MessageCountService messageCountService = new MessageCountService();
    SendService sendService = new SendService();
    // Create test companies
    Company sammyShed = new Company("company_001", "Sammy Shed",
        Arrays.asList("cart_abandonment", "product_view", "price_drop"));
    Company journeyJeans = new Company("company_002", "Journey Jeans",
        Arrays.asList("cart_abandonment", "product_view", "page_view"));
    Company sirenDenim = new Company("company_003", "Siren Denim",
        Arrays.asList("price_drop"));
    companyService.addCompany(sammyShed);
    companyService.addCompany(journeyJeans);
    companyService.addCompany(sirenDenim);
    // Create test users
    userService.addUser(new User("user123", "active", "company_001"));
    userService.addUser(new User("user456", "not_active", "company_001"));
    userService.addUser(new User("user789", "active", "company_002"));
    userService.addUser(new User("user101", "active", "company_003"));
    userService.addUser(new User("user102", "active", "company_001"));
    userService.addUser(new User("user103", "active", "company_001"));
    userService.addUser(new User("user103", "active", "company_002"));
    MessageService messageService = new MessageService(userService, companyService, messageCountService, sendService);
    // TEST 1: Non-existent user
    boolean result1 = messageService.sendMessage("user999", "cart_abandonment");
    assert result1 == false : "Test 1 Failed: Should return false for non-existent user";
    System.out.println("Test 1 Passed\n");
    // TEST 2: Inactive subscription status 
    boolean result2 = messageService.sendMessage("user456", "cart_abandonment");
    assert result2 == false : "Test 2 Failed: Should return false for inactive subscription";
    System.out.println("Test 2 Passed\n");
    // TEST 3: First message for user
    boolean result3 = messageService.sendMessage("user123", "cart_abandonment");
    assert result3 == true : "Test 3 Failed: Should successfully send first message";
    System.out.println("Test 3 Passed\n");
    // TEST 4: Message count at limit
    for (int i = 0; i < 9; i++) {
        messageService.sendMessage("user789", "cart_abandonment");
    }
    boolean result4 = messageService.sendMessage("user789", "cart_abandonment");
    assert result4 == true : "Test 4 Failed: Should return true when count is at limit";
    System.out.println("Test 4 Passed\n");
    // TEST 5: Invalid message type
    boolean result5 = messageService.sendMessage("user101", "cart_abandonment");
    assert result5 == false : "Test 5 Failed: Should return false for inactive message type";
    System.out.println("Test 5 Passed\n");
    // TEST 6: Valid message type
    boolean result6 = messageService.sendMessage("user103", "price_drop");
    assert result6 == true : "Test 6 Failed: Should successfully send price_drop to Sammy's Shed";
    System.out.println("Test 6 Passed\n");
    // TEST 7: Failed messages
    for (int i = 0; i <= 10; i++) {
        messageService.sendMessage("user102", "invalid_type");
    }
    boolean result = messageService.sendMessage("user102", "cart_abandonment");
    assert result == true : "Test 7 Failed: Should send message after failed messages";
    System.out.println("Test 7 Passed\n");
    System.out.println("All tests passed!");
  }
}