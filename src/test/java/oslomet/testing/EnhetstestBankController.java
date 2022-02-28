package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    /* Test av hentKundeInfo
     ********************************************************/

    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    /* Test av hentKonti
     ********************************************************/

    @Test
    public void hentKonti_LoggetInn()  {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);


        // act
        List<Konto> resultat = bankController.hentKonti();
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentKonti(anyString())).thenReturn(konti);

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn()  {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    /* Test av hentTransaksjoner
     ********************************************************/

    @Test
    public void hentTransaksjoner_LoggetInn() {

        Konto enKonto = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentTransaksjoner(anyString(),anyString(),anyString())).thenReturn(enKonto);

        Konto resultat = bankController.hentTransaksjoner("01010110523","2000-01-01","2000-12-12");

        assertEquals(resultat,enKonto);
    }

    @Test
    public void hentTransaksjoner_IkkeLoggetInn()  {
        // arrange
        Konto enkonto = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        // act
        when(sjekk.loggetInn()).thenReturn(null);
        //when(repository.hentTransaksjoner(anyString(),anyString(),anyString())).thenReturn(enkonto);
        Konto resultat = bankController.hentTransaksjoner("01010110523","2000-01-01","2100-01-01");

        // assert
        assertNull(resultat);
    }

    /* Test av hentSaldi
     ********************************************************/

    @Test
    public void hentSaldi_loggetInn() {
        // arrange
        List<Konto> saldi = new ArrayList<>();

        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        saldi.add(konto1);
        saldi.add(konto2);

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentSaldi(anyString())).thenReturn(saldi);
        List<Konto> resultat = bankController.hentSaldi();

        // assert
        assertEquals(saldi, resultat);
    }

    @Test
    public void hentSaldi_IkkeLoggetInn()  {
        // arrange
        List<Konto> saldi = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        saldi.add(konto1);
        saldi.add(konto2);

        // act
        when(sjekk.loggetInn()).thenReturn(null);
        when(repository.hentSaldi(anyString())).thenReturn(saldi);
        List resultat = bankController.hentSaldi();

        // assert
        assertNull(resultat);
    }

     /* Test av RegistrerBetaling
    ***************************************/

    @Test
    public void registrerBetaling_OK() {
        // arrange
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("OK");
        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertEquals("OK",resultat);
    }

    @Test
    public void registrerBetaling_Feil() {
        // arrange
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("Feil");

        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertEquals("Feil", resultat);

    }

    @Test
    public void registrerBetaling_IkkeLoggetInn()  {
        // arrange
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        // act
        when(sjekk.loggetInn()).thenReturn(null);
        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertNull(resultat);
    }

    /* Test av hentBetalinger
     ********************************************************/

    @Test
    public void hentBetalinger_loggetInn() {
        // arrange
        List<Transaksjon> hentBetalinger = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");
        Transaksjon transaksjon2 = new Transaksjon(2,"11010110523",
                1700,"12.2.2019","Overføring","Ja",
                "934567891234");
        hentBetalinger.add(transaksjon1);
        hentBetalinger.add(transaksjon2);

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentBetalinger(anyString())).thenReturn(hentBetalinger);
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // assert
        assertEquals(resultat,hentBetalinger);
    }

    @Test
    public void hentBetalinger_ikkeLoggetInn() {
        // arrange
        List<Transaksjon> hentBetalinger = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        // act
        when(sjekk.loggetInn()).thenReturn(null);
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // assert
        assertNull(resultat);
    }

    /* Test av utfortBetaling
     ********************************************************/

    @Test
    public void utforBetaling_LoggetInn() {
        // arrange
        List<Transaksjon> betalinger = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");
        Transaksjon transaksjon2 = new Transaksjon(2,"11010110523",
                1700,"12.2.2019","Overføring","Ja",
                "934567891234");
        betalinger.add(transaksjon1);
        betalinger.add(transaksjon2);

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.utforBetaling(anyInt())).thenReturn("OK");
        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);
        List<Transaksjon> resultat = bankController.utforBetaling(1);

        // assert
        assertEquals(betalinger,resultat);
    }

    @Test
    public void utforBetaling_ikkeLoggetInn() {
        // arrange
        List<Transaksjon> betalinger = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> resultat = bankController.utforBetaling(1);

        // assert
        assertNull(resultat);
    }


    /* Test av endreKundeInfo
     ********************************************************/

    @Test
    public void endreKundeInfo_OK() {
        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");
        String resultat = bankController.endre(enKunde);

        // assert
        assertEquals("OK", resultat);
    }

    @Test
    public void endreKundeInfo_Feil() {
        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // act
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("Feil");
        String resultat = bankController.endre(enKunde);

        // assert
        assertEquals("Feil", resultat);
    }

    @Test
    public void endreKundeInfo_ikkeLoggetInn() {
        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // act
        when(sjekk.loggetInn()).thenReturn(null);
        String resultat = bankController.endre(enKunde);

        // assert
        assertNull(resultat);
    }
}