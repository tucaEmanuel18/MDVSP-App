package com.labEleven.Twitter;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/MDVSP")
public class MDVSPController {

    public MDVSPController() {

    }
/*    @GetMapping("")
    public List<PersonDTO> getPersons() {
        EntityManager entityManager = managerFactory.createEntityManager();
        List<Person> result = entityManager
                .createQuery("Select p from Person p", Person.class)
                .getResultList();
        List personsDTO = new ArrayList<>();
        for(Person person : result){
            personsDTO.add(new PersonDTO(person));
        }
        return personsDTO;
    }*/

    /**
     * depot{
     *     "name" : "d1",
     *     "numberOfVehicles" : "5",
     *     "latitude" : "222.222",
     *     "longitude" : "3333.3333"
     * }
     *
     *
     */

    @PostMapping(value = "/depot")
    public ResponseEntity<String> createDepot(@RequestParam String name, @RequestParam String numberOfVehicles,
                                              @RequestParam String latitude, @RequestParam String longitude)  {
        System.out.println("name = " + name + " | "
                 + "numberOfVehicles = " + numberOfVehicles + " | "
                 + "latitude = " + latitude + " | "
                 + "longitute = " + longitude + " ");
        return new ResponseEntity<>("Depot created successfully", HttpStatus.CREATED);
    }

    /*@PutMapping(value = "/{id}")
    public ResponseEntity<String> updatePerson(
            @PathVariable long id, @RequestParam(value = "name") String name) {
        Person person = personRepository.findById(id);
        if (person == null) {
            throw new PersonNotFoundException("No person with this particular id was found. Please enter a valid id.");
        }
        personRepository.updateName(id, name);
        return new ResponseEntity<>(
                "Person updated successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePerson(
            @PathVariable long id) {
        Person person = personRepository.findById(id);
        if (person == null) {
            throw new PersonNotFoundException("No person with this particular id was found. Please enter a valid id.");
        }
        personRepository.delete(id);
        return new ResponseEntity<>(
                "Person deleted successsfully", HttpStatus.OK);
    }

    @PostMapping(value = "/{person_id}/friends/{friend_id}")
    public ResponseEntity<String> addRelation(@PathVariable(value = "person_id") long person_id,
                                              @PathVariable(value = "friend_id") long friend_id) {

        Person person = personRepository.findById(person_id);
        if (person == null) {
            throw new PersonNotFoundException("No person with this particular id was found. Please enter a valid id.");
        }
        Person friend = personRepository.findById(friend_id);
        if (friend == null) {
            throw new PersonNotFoundException("No person with this particular id was found. Please enter a valid id.");
        }
        personRepository.addFriend(person, friend);
        personRepository.addFriend(friend, person);
        return new ResponseEntity<>(
                "Relationship added with success!", HttpStatus.CREATED);
    }

    @GetMapping("/{person_id}/friends")
    @ResponseBody
    public List<String> getFriends(@PathVariable long person_id) {
        Person person = personRepository.findById(person_id);
        if (person == null) {
            throw new PersonNotFoundException("No person with this particular id was found. Please enter a valid id.");
        }
        PersonDTO personDTO = new PersonDTO(person);
        return personDTO.getFriends();
    }*/

}
