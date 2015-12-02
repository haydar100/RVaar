using MySql.Data.MySqlClient;
using RVaarWebService.Domein;
using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;


namespace RVaarWebService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in both code and config file together.
    public class RVaarServiceImpl : RVaarService
    {
        public string GetData(string value)
        {
            return string.Format("You entered: {0}", value);
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }

        public static string connectieString = "yourConnectionstringHere";

     
        public List<MarkerOption> geefKruispunten()
        {
            List<MarkerOption> array = new List<MarkerOption>();
            MySqlConnection con = new MySqlConnection();
            con.ConnectionString = connectieString;
            
            string query = "select * from graph";
            MySqlCommand cmd = new MySqlCommand(query, con);
            con.Open();
            MySqlDataReader reader = cmd.ExecuteReader();
            try
            {
                while (reader.Read())
                {
                    MarkerOption option = new MarkerOption();
                    option.zetLatitude(reader.GetFloat("y1"));
                    option.zetLongitude(reader.GetFloat("x1"));
                    option.zetNaam(reader.GetString("lnaam"));
                    array.Add(option);
                }
            }
            finally
            {
                reader.Close();
                con.Close();
             
            }
            return array;
        }

        public List<TipsTricks> geefTips()
        {
            List<TipsTricks> array = new List<TipsTricks>();
            MySqlConnection con = new MySqlConnection();
            con.ConnectionString = connectieString;

            string query = "SELECT TT.ID, TT.OMSCHRIJVING, TT.GEBRUIKER_NIVEAU, TC.OMSCHRIJVING AS CATEGORIE FROM TIPSTRICKS TT, TIPSTRICKS_CATEGORIE TC WHERE TT.CATEGORIE = TC.ID";
            MySqlCommand cmd = new MySqlCommand(query, con);
            con.Open();
            MySqlDataReader reader = cmd.ExecuteReader();
            try
            {
                while (reader.Read())
                {
                    TipsTricks tt = new TipsTricks();
                    tt.zetID(reader.GetInt32("ID"));
                    tt.zetOmschrijving(reader.GetString("OMSCHRIJVING"));
                    tt.zetGebruikerNiveau(reader.GetInt32("GEBRUIKER_NIVEAU"));
                    tt.zetCategorie(reader.GetString("CATEGORIE"));
                    array.Add(tt);
                }
            }
            finally
            {
                reader.Close();
                con.Close();

            }
            return array;
        }



        public List<TipsTricks> geefTipsBijCategorie(string categorieNaam)
        {
            List<TipsTricks> array = new List<TipsTricks>();
            MySqlConnection con = new MySqlConnection();
            con.ConnectionString = connectieString;

            string query = "SELECT TT.ID, TT.OMSCHRIJVING, TT.GEBRUIKER_NIVEAU, TC.OMSCHRIJVING AS CATEGORIE FROM TIPSTRICKS TT, TIPSTRICKS_CATEGORIE TC WHERE TT.CATEGORIE = TC.ID AND TC.OMSCHRIJVING = '" + categorieNaam + "'";
            MySqlCommand cmd = new MySqlCommand(query, con);
            con.Open();
            MySqlDataReader reader = cmd.ExecuteReader();
            try
            {
                while (reader.Read())
                {
                    TipsTricks tt = new TipsTricks();
                    tt.zetID(reader.GetInt32("ID"));
                    tt.zetOmschrijving(reader.GetString("OMSCHRIJVING"));
                    tt.zetGebruikerNiveau(reader.GetInt32("GEBRUIKER_NIVEAU"));
                    tt.zetCategorie(reader.GetString("CATEGORIE"));
                    array.Add(tt);
                }
            }
            finally
            {
                reader.Close();
                con.Close();

            }
            return array;
        }
    }
}
