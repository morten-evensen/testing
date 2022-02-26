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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

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

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        Konto resultat = bankController.hentTransaksjoner(anyString(),anyString(),anyString());

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentSaldi_loggetInn() {
        List<Konto> saldi = new ArrayList<>();

        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        saldi.add(konto1);
        saldi.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentSaldi(anyString())).thenReturn(saldi);

        List<Konto> resultat = bankController.hentSaldi();

        assertEquals(saldi, resultat);
    }

    @Test
    public void hentSaldi_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List resultat = bankController.hentSaldi();

        // assert
        assertNull(resultat);
    }

     /* RegistrerBetaling
       HentBetalinger
       UtforBetalinger
       EndreKundeInfo
       HentTransaksjoner!!!
    *******************/
    @Test
    public void registrerBetaling_OK() {
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("OK");

        String resultat = bankController.registrerBetaling(enTransaksjon);

        assertEquals("OK",resultat);
    }

    @Test
    public void registrerBetaling_Feil() {
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn("Feil");

        String resultat = bankController.registrerBetaling(enTransaksjon);

        assertEquals("Feil", resultat);

    }

    @Test
    public void registrerBetaling_IkkeLoggetInn()  {
        Transaksjon enTransaksjon = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertNull(resultat);
    }


    @Test
    public void hentBetalinger() {
        List<Transaksjon> betalinger = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");
        Transaksjon transaksjon2 = new Transaksjon(2,"11010110523",
                1700,"12.2.2019","Overføring","Ja",
                "934567891234");
        betalinger.add(transaksjon1);
        betalinger.add(transaksjon2);
        //Logg inn
        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);

        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertEquals(resultat,betalinger);
    }

    /* Altså... hmm......
     ****************/

    @Test
    public void utforBetaling() {
        List<Transaksjon> utforBetaling1 = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1,"01010110523",
                700,"12.2.2019","Overføring","Ja",
                "1234567891234");
        Transaksjon transaksjon2 = new Transaksjon(2,"11010110523",
                1700,"12.2.2019","Overføring","Ja",
                "934567891234");
        utforBetaling1.add(transaksjon1);
        utforBetaling1.add(transaksjon2);

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.utforBetaling(anyInt())).thenReturn("OK");

        List<Transaksjon> resultat = bankController.utforBetaling(1);

        assertEquals(utforBetaling1, resultat);
    }

    @Test
    public void endreKundeInfo_OK() {
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");

        String resultat = bankController.endre(enKunde);

        assertEquals("OK", resultat);
    }

    @Test
    public void endreKundeInfo_Feil() {
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("Feil");

        String resultat = bankController.endre(enKunde);

        assertEquals("Feil", resultat);
    }

}

