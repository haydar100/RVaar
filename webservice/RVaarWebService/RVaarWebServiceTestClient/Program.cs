using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using RVaarWebServiceTestClient.ServiceReference1;

namespace RVaarWebServiceTestClient
{
    class Program
    {
        static void Main(string[] args)
        {
            Service1Client client = new Service1Client("BasicHttpBinding_IService1");
            client.Open();
            Console.WriteLine("Service opened");
            Console.ReadLine();
        }
    }
}
