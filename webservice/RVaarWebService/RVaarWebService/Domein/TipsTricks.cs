using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RVaarWebService.Domein
{
    public class TipsTricks
    {
        private int ID, gebruikerNiveau;
        private string omschrijving, categorie;

        public TipsTricks()
        {

        }

        public void zetID(int ID)
        {
            this.ID = ID;
        }

        public void zetGebruikerNiveau(int niveau)
        {
            this.gebruikerNiveau = niveau;
        }

        public void zetOmschrijving(string omschrijving)
        {
            this.omschrijving = omschrijving;
        }

        public int geefID()
        {
            return this.ID;
        }

        public int geefNiveau()
        {
            return this.gebruikerNiveau;
        }

        public string geefOmschrijving()
        {
            return this.omschrijving;
        }

        public void zetCategorie(string omschrijving)
        {
            this.categorie = omschrijving;
        }

        public string geefCategorie()
        {
            return this.categorie;
        }
    }
}
