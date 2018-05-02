package fundamentals;
import static java.lang.Math.random;
import static java.lang.System.out;
import java.util.Date;
/**
 * A {@code Person} object represents a person.
 * @author Pavel F
 * @version 1.0
 * @since 25.12.17
 * @deprecatedNo
 * @see <a href="google.com">google</a>
 * @see fundamentals.FileOI#main
 * */
public class DocumentedPerson {

	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private long id;
	public static long currentNo;
	
	{
		System.out.println("Initialization block. Calls before constructor.");
		dateOfBirth = new Date();
		currentNo++;
	}
	
	static {
		System.out.println("This is static initialization block. "
				+ "Occurs when the class is first loaded.");
	}
	
	public DocumentedPerson(long id, String firstName, String lastName) {
		System.out.println("Constructor is being called.");
		this.firstName = firstName;
		this.lastName = lastName;
		id = (long) (random() * id);
	}

	public static void main(String[] args) {
		DocumentedPerson person = new DocumentedPerson(100, "Anna", "Lee");
		DocumentedPerson jd = new DocumentedPerson(100, "John", "Doe");
		out.println(person);
	}

	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name of the {@link <a href="google.com">google</a>} person
	 * @param firstName represents first name
	 * @return returns nothing - void
	 * */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return (Date) dateOfBirth.clone();
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "DocumentedPerson [firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
				+ ", id=" + id + "]";
	}

}
