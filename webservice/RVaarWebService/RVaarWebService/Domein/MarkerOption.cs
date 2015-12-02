using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RVaarWebService
{
    public class MarkerOption
    {
        private float latitude, longitude;
        private string naam;

        public MarkerOption()
        {

        }

        public MarkerOption(float latitude, float longitude, string naam)
        {
            zetLatitude(latitude);
            zetLongitude(longitude);
            zetNaam(naam);
        }

        public void zetLatitude(float input)
        {
            latitude = input;
        }

        public void zetLongitude(float input)
        {
            longitude = input;
        }

        public void zetNaam(string input)
        {
            naam = input;
        }

        public float geefLatitude()
        {
            return latitude;
        }

        public float geefLongitude()
        {
            return longitude;
        }

        public string geefNaam()
        {
            return naam;
        }
    }
}
