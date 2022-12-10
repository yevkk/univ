using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDB_CW.Models
{
    public class Row
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("mnemonic")]
        public string? Mnemonic { get; set; }

        [Column("places_number")]
        [Display(Name = "Number of places")]
        public int? PlacesNumber { get; set; }

        [ForeignKey("Hall")]
        [Column("hall_id")]
        [Display(Name = "Hall")]
        public int HallId { get; set; }

        public Hall? Hall { get; set; }
    }
}
