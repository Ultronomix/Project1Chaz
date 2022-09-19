package reimbursements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;

public class ReimbServiceTest {

    ReimbService sut;
    ReimbursementsDAO mockReimbDAO;
    private HashSet<Reimbursements> Reimbs;

    @BeforeEach
    public void setUp() {
        mockReimbDAO = Mockito.mock(ReimbursementsDAO.class);
        sut = new ReimbService(mockReimbDAO);
    }

    @AfterEach
    public void cleanUp () {
        Mockito.reset(mockReimbDAO);
    }

    @Test
    public void testGetAllReimb() {

        Reimbursements reimb1 = new Reimbursements();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted(LocalDateTime.parse("time"));
        reimb1.setResolved(LocalDateTime.parse("resolved"));
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolved_id("resolver_id");
        reimb1.setStatus_id("status");
        reimb1.setType_id("type");

        Reimbursements reimb2 = new Reimbursements();
        reimb2.setReimb_id("2");
        reimb2.setAmount(300);
        reimb2.setSubmitted(LocalDateTime.parse("time"));
        reimb2.setResolved(LocalDateTime.parse("resolved"));
        reimb2.setDescription("description");
        reimb2.setPayment_id("payment_id");
        reimb2.setAuthor_id("author_id");
        reimb2.setResolved_id("resolver_id");
        reimb2.setStatus_id("status");
        reimb2.setType_id("type");

        List<ReimbursementsResponse> results = new ArrayList<>();
        List<Reimbursements> reimbs = new ArrayList<>();

        reimbs.add(reimb1);
        reimbs.add(reimb2);


        for (Reimbursements reimb : reimbs) {
            results.add(new ReimbursementsResponse(reimb));
        }

        when(mockReimbDAO.getAllReimbs()).thenReturn(reimbs);

        List<ReimbursementsResponse> expected = results;

        List<ReimbursementsResponse> actual = sut.getAllReimbs();

        assertNotNull(results);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbById() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByReimb_id("");
        });

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByReimb_id(null);
        });

        Reimbursements reimb = new Reimbursements();
        when(mockReimbDAO.getReimbByReimbId(anyString())).thenReturn(Optional.of(reimb));

        ReimbursementsResponse actual = sut.getReimbByReimb_id("2");
        ReimbursementsResponse expected = new ReimbursementsResponse(reimb);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbByUserId() {

        Reimbursements reimb1 = new Reimbursements();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted(LocalDateTime.parse("time"));
        reimb1.setResolved(LocalDateTime.parse("resolved"));
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolved_id("resolver_id");
        reimb1.setStatus_id("status");
        reimb1.setType_id("type");

        List<ReimbursementsResponse> results = new ArrayList<>();
        List<Reimbursements> reimbs = new ArrayList<>();

        reimbs.add(reimb1);

        for (Reimbursements reimb : reimbs) {
            results.add(new ReimbursementsResponse(reimb));
        }

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByReimb_id("2");
        });

        when(mockReimbDAO.getAllReimbs()).thenReturn(reimbs);

        List<ReimbursementsResponse> actual = (List<ReimbursementsResponse>) sut.getReimbByReimb_id("1");
        List<ReimbursementsResponse> expected = results;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbByStatus() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus(null);
        });

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus("status");
        });

        List<ReimbursementsResponse> result = new ArrayList<>();
        List<ReimbursementsResponse> reimbs = new ArrayList<>();

        Reimbursements reimb1 = new Reimbursements();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted(LocalDateTime.parse("time"));
        reimb1.setResolved(LocalDateTime.parse("resolved"));
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolved_id("resolver_id");
        reimb1.setStatus_id("PENDING");
        reimb1.setType_id("type");

        Reimbs.add(reimb1);

        for (ReimbursementsResponse reimb : reimbs) {
            result.add(new ReimbursementsResponse((Reimbursements) reimbs));
        }

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByStatus("Approved");
        });

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByStatus("Denied");
        });

        when(mockReimbDAO.getReimbByStatus(anyString())).thenReturn(null);

        ReimbursementsResponse actual = sut.getReimbByStatus("Pending");
        List<ReimbursementsResponse> expected = result;

        assertEquals(expected, actual);
    }



    @Test
    public void testCreateRequest() {

        NewReimbursementRequest newRequest = new NewReimbursementRequest();

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(null);
        });

        newRequest.setReimb_id(null);
        newRequest.setAmount(200);

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setReimb_id("");

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setReimb_id("1");
        newRequest.setAmount(0);

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setAmount(10000);

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setAmount(500);

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setDescription("");

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setDescription("description");

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });


        newRequest.setType_id("");

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setType_id("wrong");

        assertThrows(InvalidRequestException.class, () -> {
            sut.create(newRequest);
        });

        newRequest.setType_id("Lodging");

        when(mockReimbDAO.register(newRequest.extractEntity())).thenReturn("Request Created ID: " + newRequest.getReimb_id());
        ResourceCreationResponse actualL = sut.create(newRequest);
        ResourceCreationResponse expectedL = new ResourceCreationResponse(null);
        assertEquals(expectedL, actualL);

        newRequest.setType_id("Travel");

        when(mockReimbDAO.register(newRequest.extractEntity())).thenReturn("Request Created ID: " + newRequest.getReimb_id());
        ResourceCreationResponse actualT = sut.create(newRequest);
        ResourceCreationResponse expectedT = new ResourceCreationResponse(null);
        assertEquals(expectedT, actualT);

        newRequest.setType_id("Food");

        when(mockReimbDAO.register(newRequest.extractEntity())).thenReturn("Request Created ID: " + newRequest.getReimb_id());
        ResourceCreationResponse actualF = sut.create(newRequest);
        ResourceCreationResponse expectedF = new ResourceCreationResponse(null);
        assertEquals(expectedF, actualF);

        newRequest.setType_id("Other");

        when(mockReimbDAO.register(newRequest.extractEntity())).thenReturn("Request Created ID: " + newRequest.getReimb_id());
        ResourceCreationResponse actualO = sut.create(newRequest);
        ResourceCreationResponse expectedO = new ResourceCreationResponse(null);
        assertEquals(expectedO, actualO);

     }

}