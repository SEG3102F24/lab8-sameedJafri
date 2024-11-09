package seg3x02.employeeGql.resolvers

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import seg3x02.employeeGql.repository.EmployeeRepository
import java.util.*

@Controller
class EmployeesResolver(@Autowired private val employeeRepository: EmployeeRepository) {

    @QueryMapping
    fun employees(): List<Employee> {
        return employeeRepository.findAll()
    }

    @QueryMapping
    fun employeeById(@Argument employeeId: String): Employee? {
        return employeeRepository.findById(employeeId).orElse(null)
    }

    @MutationMapping
    fun addEmployee(@Argument createEmployeeInput: CreateEmployeeInput): Employee {
        val newEmployee = Employee(
            name = createEmployeeInput.name ?: throw IllegalArgumentException("Name is required"),
            dateOfBirth = createEmployeeInput.dateOfBirth ?: throw IllegalArgumentException("Date of Birth is required"),
            city = createEmployeeInput.city ?: throw IllegalArgumentException("City is required"),
            salary = createEmployeeInput.salary ?: throw IllegalArgumentException("Salary is required"),
            gender = createEmployeeInput.gender,
            email = createEmployeeInput.email
        )
        newEmployee.id = UUID.randomUUID().toString()
    }

    @MutationMapping
    fun updateEmployee(@Argument employeeId: String, @Argument createEmployeeInput: CreateEmployeeInput): Employee? {
        val existingEmployee = employeeRepository.findById(employeeId).orElse(null)
        return if (existingEmployee != null) {
            existingEmployee.apply {
                name = createEmployeeInput.name ?: name
                dateOfBirth = createEmployeeInput.dateOfBirth ?: dateOfBirth
                city = createEmployeeInput.city ?: city
                salary = createEmployeeInput.salary ?: salary
                gender = createEmployeeInput.gender ?: gender
                email = createEmployeeInput.email ?: email
            }
            employeeRepository.save(existingEmployee)
        } else {
            null
        }
    }

    @MutationMapping
    fun deleteEmployee(@Argument employeeId: String): Boolean {
        return if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId)
            true
        } else {
            false
        }
    }


}
