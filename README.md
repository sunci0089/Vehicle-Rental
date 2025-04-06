# Iznajmljivanje vozila

**Swing GUI multithread aplikacija implementirana u Java programskom jeziku**

---

## Specifikacija funkcionalnosti

`ePJ2` je e-mobility kompanija koja se bavi iznajmljivanjem električnih **automobila**, **bicikala** i **trotineta**, na užem i širem prostoru grada.  
Cilj projekta je da se napravi program koji simulira korišćenje prevoznih sredstava na osnovu predefinisanih podataka i generiše detaljne:

- finansijske obračune,  
- statistike
- praćenje stanja svih prevoznih sredstava.

---

### Prevozna sredstva

- Automobili, električni bicikli i električni trotineti.
- Sva prevozna sredstva se mogu **pokvariti** (evidentira se opis i vrijeme kvara).
- Imaju mogućnost **punjenja i pražnjenja baterije**.

---

### Proces iznajmljivanja

Prilikom iznajmljivanja bilježe se:
- datum i vrijeme iznajmljivanja,
- ime korisnika,
- početna i krajnja lokacija,
- trajanje u sekundama.

Na osnovu ovih podataka:
- generiše se **račun** (`.txt` fajl) sa cijenom,
- cijena se računa po tipu vozila i trajanju.

---

### Simulacija

Podaci se učitavaju redom, sortirano po vremenu iznajmljivanja.  
Za **svaki red**:
- kreira se **odvojena nit** koja pokreće simulaciju kretanja po mapi.
- koristi se **zajednička mapa** 20x20 za sve niti.

Pravila simulacije:
- Traje onoliko sekundi koliko traje iznajmljivanje.
- Putanja između početne i krajnje tačke je **pravolinijska**.
- Kretanje se prikazuje promjenom boje polja, prikazom ID-a i nivoa baterije.
- Nakon završetka simulacije za određeni datum – pauza od 5 sekundi.
- Nakon svakog iznajmljivanja kreira se **račun** sa:
  - detaljima,
  - ukupnom cijenom,
  - popustima/promocijama (ako postoje).

**Slika mape**:

![image](https://github.com/user-attachments/assets/ff0b5ca7-a9a0-48fe-a0bb-9b94e3a1d195)

---

### 🖥️ Grafički interfejsi

1. Glavni ekran – prikaz mape  
2. Ekran za prikaz prevoznih sredstava – 3 tabele  
3. Ekran za prikaz kvarova – tabela sa:
   - vrstom,
   - ID-em,
   - vremenom,
   - opisom kvara  
4. Ekran za prikaz rezultata poslovanja

---

### Sumarni i dnevni izvještaj

- Prikazuje stavke **grupisane po datumu**
- **Tabelarni prikaz**
- Prikaz svih dostupnih datuma

---

### Serijalizacija i deserijalizacija

- Podaci o najprofitabilnijim vozilima se **serijalizuju binarno**
- Čuvaju se:
  - Objekat vozila sa svim atributima
  - Ukupan prihod koji je ostvario
- Smještaju se u zadati folder
- Postoji ekran za **deserijalizaciju i prikaz** tih podataka

