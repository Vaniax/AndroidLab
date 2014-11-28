
package instameet.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the instameet.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateAppointments_QNAME = new QName("http://service/", "CreateAppointments");
    private final static QName _GetFriends_QNAME = new QName("http://service/", "GetFriends");
    private final static QName _CreateUserResponse_QNAME = new QName("http://service/", "createUserResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://service/", "loginResponse");
    private final static QName _AddFriend_QNAME = new QName("http://service/", "addFriend");
    private final static QName _UpdateLocationResponse_QNAME = new QName("http://service/", "UpdateLocationResponse");
    private final static QName _GetAppointmentsResponse_QNAME = new QName("http://service/", "GetAppointmentsResponse");
    private final static QName _GetFriendLocationResponse_QNAME = new QName("http://service/", "GetFriendLocationResponse");
    private final static QName _GetFriendLocationsResponse_QNAME = new QName("http://service/", "GetFriendLocationsResponse");
    private final static QName _GetOwnDataResponse_QNAME = new QName("http://service/", "getOwnDataResponse");
    private final static QName _VisitAppointmentResponse_QNAME = new QName("http://service/", "visitAppointmentResponse");
    private final static QName _GetFriendLocations_QNAME = new QName("http://service/", "GetFriendLocations");
    private final static QName _CreateAppointmentsResponse_QNAME = new QName("http://service/", "CreateAppointmentsResponse");
    private final static QName _AddFriendResponse_QNAME = new QName("http://service/", "addFriendResponse");
    private final static QName _CreateUser_QNAME = new QName("http://service/", "createUser");
    private final static QName _GetOwnData_QNAME = new QName("http://service/", "getOwnData");
    private final static QName _VerifyUserResponse_QNAME = new QName("http://service/", "verifyUserResponse");
    private final static QName _GetFriendsResponse_QNAME = new QName("http://service/", "GetFriendsResponse");
    private final static QName _Login_QNAME = new QName("http://service/", "login");
    private final static QName _VisitAppointment_QNAME = new QName("http://service/", "visitAppointment");
    private final static QName _UpdateLocation_QNAME = new QName("http://service/", "UpdateLocation");
    private final static QName _VerifyUser_QNAME = new QName("http://service/", "verifyUser");
    private final static QName _GetFriendLocation_QNAME = new QName("http://service/", "GetFriendLocation");
    private final static QName _SendMessageResponse_QNAME = new QName("http://service/", "SendMessageResponse");
    private final static QName _GetAppointments_QNAME = new QName("http://service/", "GetAppointments");
    private final static QName _GetMessagesResponse_QNAME = new QName("http://service/", "GetMessagesResponse");
    private final static QName _SendMessage_QNAME = new QName("http://service/", "SendMessage");
    private final static QName _GetMessages_QNAME = new QName("http://service/", "GetMessages");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: instameet.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link User.HostedAppointments }
     * 
     */
    public User.HostedAppointments createUserHostedAppointments() {
        return new User.HostedAppointments();
    }

    /**
     * Create an instance of {@link User.Friends }
     * 
     */
    public User.Friends createUserFriends() {
        return new User.Friends();
    }

    /**
     * Create an instance of {@link UpdateLocation }
     * 
     */
    public UpdateLocation createUpdateLocation() {
        return new UpdateLocation();
    }

    /**
     * Create an instance of {@link VerifyUser }
     * 
     */
    public VerifyUser createVerifyUser() {
        return new VerifyUser();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link VisitAppointment }
     * 
     */
    public VisitAppointment createVisitAppointment() {
        return new VisitAppointment();
    }

    /**
     * Create an instance of {@link GetFriendsResponse }
     * 
     */
    public GetFriendsResponse createGetFriendsResponse() {
        return new GetFriendsResponse();
    }

    /**
     * Create an instance of {@link GetAppointments }
     * 
     */
    public GetAppointments createGetAppointments() {
        return new GetAppointments();
    }

    /**
     * Create an instance of {@link GetMessagesResponse }
     * 
     */
    public GetMessagesResponse createGetMessagesResponse() {
        return new GetMessagesResponse();
    }

    /**
     * Create an instance of {@link SendMessageResponse }
     * 
     */
    public SendMessageResponse createSendMessageResponse() {
        return new SendMessageResponse();
    }

    /**
     * Create an instance of {@link GetFriendLocation }
     * 
     */
    public GetFriendLocation createGetFriendLocation() {
        return new GetFriendLocation();
    }

    /**
     * Create an instance of {@link GetMessages }
     * 
     */
    public GetMessages createGetMessages() {
        return new GetMessages();
    }

    /**
     * Create an instance of {@link SendMessage }
     * 
     */
    public SendMessage createSendMessage() {
        return new SendMessage();
    }

    /**
     * Create an instance of {@link AddFriend }
     * 
     */
    public AddFriend createAddFriend() {
        return new AddFriend();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link CreateUserResponse }
     * 
     */
    public CreateUserResponse createCreateUserResponse() {
        return new CreateUserResponse();
    }

    /**
     * Create an instance of {@link CreateAppointments }
     * 
     */
    public CreateAppointments createCreateAppointments() {
        return new CreateAppointments();
    }

    /**
     * Create an instance of {@link GetFriends }
     * 
     */
    public GetFriends createGetFriends() {
        return new GetFriends();
    }

    /**
     * Create an instance of {@link GetFriendLocationsResponse }
     * 
     */
    public GetFriendLocationsResponse createGetFriendLocationsResponse() {
        return new GetFriendLocationsResponse();
    }

    /**
     * Create an instance of {@link GetFriendLocationResponse }
     * 
     */
    public GetFriendLocationResponse createGetFriendLocationResponse() {
        return new GetFriendLocationResponse();
    }

    /**
     * Create an instance of {@link GetAppointmentsResponse }
     * 
     */
    public GetAppointmentsResponse createGetAppointmentsResponse() {
        return new GetAppointmentsResponse();
    }

    /**
     * Create an instance of {@link UpdateLocationResponse }
     * 
     */
    public UpdateLocationResponse createUpdateLocationResponse() {
        return new UpdateLocationResponse();
    }

    /**
     * Create an instance of {@link VisitAppointmentResponse }
     * 
     */
    public VisitAppointmentResponse createVisitAppointmentResponse() {
        return new VisitAppointmentResponse();
    }

    /**
     * Create an instance of {@link GetOwnDataResponse }
     * 
     */
    public GetOwnDataResponse createGetOwnDataResponse() {
        return new GetOwnDataResponse();
    }

    /**
     * Create an instance of {@link CreateUser }
     * 
     */
    public CreateUser createCreateUser() {
        return new CreateUser();
    }

    /**
     * Create an instance of {@link GetOwnData }
     * 
     */
    public GetOwnData createGetOwnData() {
        return new GetOwnData();
    }

    /**
     * Create an instance of {@link VerifyUserResponse }
     * 
     */
    public VerifyUserResponse createVerifyUserResponse() {
        return new VerifyUserResponse();
    }

    /**
     * Create an instance of {@link CreateAppointmentsResponse }
     * 
     */
    public CreateAppointmentsResponse createCreateAppointmentsResponse() {
        return new CreateAppointmentsResponse();
    }

    /**
     * Create an instance of {@link AddFriendResponse }
     * 
     */
    public AddFriendResponse createAddFriendResponse() {
        return new AddFriendResponse();
    }

    /**
     * Create an instance of {@link GetFriendLocations }
     * 
     */
    public GetFriendLocations createGetFriendLocations() {
        return new GetFriendLocations();
    }

    /**
     * Create an instance of {@link Chatmessage }
     * 
     */
    public Chatmessage createChatmessage() {
        return new Chatmessage();
    }

    /**
     * Create an instance of {@link Appointment }
     * 
     */
    public Appointment createAppointment() {
        return new Appointment();
    }

    /**
     * Create an instance of {@link LoginData }
     * 
     */
    public LoginData createLoginData() {
        return new LoginData();
    }

    /**
     * Create an instance of {@link Timestamp }
     * 
     */
    public Timestamp createTimestamp() {
        return new Timestamp();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link Time }
     * 
     */
    public Time createTime() {
        return new Time();
    }

    /**
     * Create an instance of {@link User.HostedAppointments.Entry }
     * 
     */
    public User.HostedAppointments.Entry createUserHostedAppointmentsEntry() {
        return new User.HostedAppointments.Entry();
    }

    /**
     * Create an instance of {@link User.Friends.Entry }
     * 
     */
    public User.Friends.Entry createUserFriendsEntry() {
        return new User.Friends.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAppointments }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "CreateAppointments")
    public JAXBElement<CreateAppointments> createCreateAppointments(CreateAppointments value) {
        return new JAXBElement<CreateAppointments>(_CreateAppointments_QNAME, CreateAppointments.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriends }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriends")
    public JAXBElement<GetFriends> createGetFriends(GetFriends value) {
        return new JAXBElement<GetFriends>(_GetFriends_QNAME, GetFriends.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "createUserResponse")
    public JAXBElement<CreateUserResponse> createCreateUserResponse(CreateUserResponse value) {
        return new JAXBElement<CreateUserResponse>(_CreateUserResponse_QNAME, CreateUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFriend }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "addFriend")
    public JAXBElement<AddFriend> createAddFriend(AddFriend value) {
        return new JAXBElement<AddFriend>(_AddFriend_QNAME, AddFriend.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateLocationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "UpdateLocationResponse")
    public JAXBElement<UpdateLocationResponse> createUpdateLocationResponse(UpdateLocationResponse value) {
        return new JAXBElement<UpdateLocationResponse>(_UpdateLocationResponse_QNAME, UpdateLocationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAppointmentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetAppointmentsResponse")
    public JAXBElement<GetAppointmentsResponse> createGetAppointmentsResponse(GetAppointmentsResponse value) {
        return new JAXBElement<GetAppointmentsResponse>(_GetAppointmentsResponse_QNAME, GetAppointmentsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriendLocationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriendLocationResponse")
    public JAXBElement<GetFriendLocationResponse> createGetFriendLocationResponse(GetFriendLocationResponse value) {
        return new JAXBElement<GetFriendLocationResponse>(_GetFriendLocationResponse_QNAME, GetFriendLocationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriendLocationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriendLocationsResponse")
    public JAXBElement<GetFriendLocationsResponse> createGetFriendLocationsResponse(GetFriendLocationsResponse value) {
        return new JAXBElement<GetFriendLocationsResponse>(_GetFriendLocationsResponse_QNAME, GetFriendLocationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOwnDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getOwnDataResponse")
    public JAXBElement<GetOwnDataResponse> createGetOwnDataResponse(GetOwnDataResponse value) {
        return new JAXBElement<GetOwnDataResponse>(_GetOwnDataResponse_QNAME, GetOwnDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisitAppointmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "visitAppointmentResponse")
    public JAXBElement<VisitAppointmentResponse> createVisitAppointmentResponse(VisitAppointmentResponse value) {
        return new JAXBElement<VisitAppointmentResponse>(_VisitAppointmentResponse_QNAME, VisitAppointmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriendLocations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriendLocations")
    public JAXBElement<GetFriendLocations> createGetFriendLocations(GetFriendLocations value) {
        return new JAXBElement<GetFriendLocations>(_GetFriendLocations_QNAME, GetFriendLocations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAppointmentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "CreateAppointmentsResponse")
    public JAXBElement<CreateAppointmentsResponse> createCreateAppointmentsResponse(CreateAppointmentsResponse value) {
        return new JAXBElement<CreateAppointmentsResponse>(_CreateAppointmentsResponse_QNAME, CreateAppointmentsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFriendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "addFriendResponse")
    public JAXBElement<AddFriendResponse> createAddFriendResponse(AddFriendResponse value) {
        return new JAXBElement<AddFriendResponse>(_AddFriendResponse_QNAME, AddFriendResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "createUser")
    public JAXBElement<CreateUser> createCreateUser(CreateUser value) {
        return new JAXBElement<CreateUser>(_CreateUser_QNAME, CreateUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOwnData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getOwnData")
    public JAXBElement<GetOwnData> createGetOwnData(GetOwnData value) {
        return new JAXBElement<GetOwnData>(_GetOwnData_QNAME, GetOwnData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "verifyUserResponse")
    public JAXBElement<VerifyUserResponse> createVerifyUserResponse(VerifyUserResponse value) {
        return new JAXBElement<VerifyUserResponse>(_VerifyUserResponse_QNAME, VerifyUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriendsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriendsResponse")
    public JAXBElement<GetFriendsResponse> createGetFriendsResponse(GetFriendsResponse value) {
        return new JAXBElement<GetFriendsResponse>(_GetFriendsResponse_QNAME, GetFriendsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisitAppointment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "visitAppointment")
    public JAXBElement<VisitAppointment> createVisitAppointment(VisitAppointment value) {
        return new JAXBElement<VisitAppointment>(_VisitAppointment_QNAME, VisitAppointment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateLocation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "UpdateLocation")
    public JAXBElement<UpdateLocation> createUpdateLocation(UpdateLocation value) {
        return new JAXBElement<UpdateLocation>(_UpdateLocation_QNAME, UpdateLocation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "verifyUser")
    public JAXBElement<VerifyUser> createVerifyUser(VerifyUser value) {
        return new JAXBElement<VerifyUser>(_VerifyUser_QNAME, VerifyUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFriendLocation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetFriendLocation")
    public JAXBElement<GetFriendLocation> createGetFriendLocation(GetFriendLocation value) {
        return new JAXBElement<GetFriendLocation>(_GetFriendLocation_QNAME, GetFriendLocation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "SendMessageResponse")
    public JAXBElement<SendMessageResponse> createSendMessageResponse(SendMessageResponse value) {
        return new JAXBElement<SendMessageResponse>(_SendMessageResponse_QNAME, SendMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAppointments }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetAppointments")
    public JAXBElement<GetAppointments> createGetAppointments(GetAppointments value) {
        return new JAXBElement<GetAppointments>(_GetAppointments_QNAME, GetAppointments.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetMessagesResponse")
    public JAXBElement<GetMessagesResponse> createGetMessagesResponse(GetMessagesResponse value) {
        return new JAXBElement<GetMessagesResponse>(_GetMessagesResponse_QNAME, GetMessagesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "SendMessage")
    public JAXBElement<SendMessage> createSendMessage(SendMessage value) {
        return new JAXBElement<SendMessage>(_SendMessage_QNAME, SendMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessages }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "GetMessages")
    public JAXBElement<GetMessages> createGetMessages(GetMessages value) {
        return new JAXBElement<GetMessages>(_GetMessages_QNAME, GetMessages.class, null, value);
    }

}
