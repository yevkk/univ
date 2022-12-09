using System.ComponentModel.DataAnnotations.Schema;

namespace DDB_CW.Models
{
    public class Hall
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("name")]
        public string? Name { get; set; }

        [Column("floor")]
        public int? Floor { get; set; }
    }
}
