<template>
  <!-- Modal -->
  <div
    class="modal fade"
    id="BasketPackingModal"
    tabindex="-1"
    role="dialog"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header border-0 pl-0">
          <h5
            class="modal-title w-100 text-center font-weight-bold position-absolute"
            id="exampleModalLabel"
          >Packing List</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <!-- modal body -->
        <div class="modal-body">
          <!-- 웹 -->
          <div class="mb-4" v-for="(post, index) in prePosts" :key="index">
            <div class="row">
              <div class="col-4 col-sm-4 col-md-4">
                <img
                  :src="makeimgurl(post.imgurl)"
                  v-if="post.imgurl"
                  alt
                  @click="getdetail(post.pid)"
                  data-dismiss="modal"
                />
              </div>
              <div
                type="text"
                class="basket-list col-8 col-sm-8 col-md-8"
                aria-label="Text input with checkbox"
                data-dismiss="modal"
                @click="getdetail(post.pid)"
              >
                <p class="mb-0">제목 : {{ post.title }}</p>
                <p class="mb-0">기간 : {{ post.sdate }}~{{ post.edate }}</p>
                <p class="mb-0">위치 : {{ post.location }}</p>
                <p class="mb-0">가격 : {{ addComma(post.price) }}원</p>
              </div>
            </div>
            <hr />
          </div>
          <!-- 가격 -->
          <!-- <p class="packaging-price mb-1">Singled Price : {{ Singleprice }}</p> -->
          <div class="d-flex justify-content-between">
            <div class="d-flex m-0"  style="width:40%" >
              <span class="d-flex my-auto mr-2" style="font-weight:bold;">수량</span>
              <button
                class="btn btn-deault d-flex"
                style="border:1px solid;background-color:#86a5d4;color:white;"
                @click="changenum(-1)"
              >
                <i class="fas fa-caret-down"></i>
              </button>
              <input
                type="number"
                onkeyup="this.value=this.value.replace(/[^0-9]/g,'');"
                v-model="sizecnt"
                class="text-center d-flex"
                style="border-radius:3px;border:1px solid lightgray;width:25%;"
              />

              <button
                class="btn btn-deault d-flex"
                style="border:1px solid;background-color:#86a5d4;color:white;"
                @click="changenum(1)"
              >
                <i class="fas fa-caret-up"></i>
              </button>
            </div>
            <div class="d-flex justify-content-end" style="width:60%">
            <p class="packaging-price mb-1">
              <span v-if="discount > 0" style="font-size:1.3rem;">{{this.discount}}% OFF</span>
              PRICE : {{ addComma(Packagingprice * this.sizecnt) }} 원
            </p>
            </div>
          </div>
        </div>

        <!-- footer -->
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-default"
            data-dismiss="modal"
            style="font-weight:bold; color:white; background-color:crimson;"
          >
            <i class="fas fa-times mr-2"></i>
            취소
          </button>
          <button
            type="button"
            class="btn btn-default"
            @click="purchase"
            style="background-color:#86a5d4;color:white;font-weight:bold;"
          >
            <i class="far fa-hand-point-up mr-2"></i>구매하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/basketmodal.css";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  created() {
    this.authUser();
  },
  props: {
    prePosts: Array,
  },
  watch: {
    sizecnt: function (v) {
      if (v >= 100) {
        this.sizecnt = 99;
      }
      if (v <= 0) {
        this.sizecnt = 1;
      }
    },
  },
  methods: {
    addComma(num) {
      var regexp = /\B(?=(\d{3})+(?!\d))/g;
      return num.toString().replace(regexp, ',');
    },
    changenum(num) {
      if (this.sizecnt + num != 0) {
        this.sizecnt += num;
      }
    },
    getdetail(pid) {
      this.$router.push({
        name: "PostListDetail",
        params: { ID: pid },
      });
    },
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          // this.Singleprice();
          this.Packagingprice();
        })
        .catch((err) => {
          // console.log(err);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/" + imgurl;
      return url;
    },
    purchase() {
      for (var i = 0; i < this.prePosts.length; i++) {
        this.packPost.push(this.prePosts[i].pid);
      }

      let th = this;

      var IMP = window.IMP; // 생략가능
      var msg;
      IMP.init("imp40062977");

      IMP.request_pay(
        {
          pg: "html5_inicis",
          pay_method: "card",
          merchant_uid: "merchant_" + new Date().getTime(),
          name: "링키비티",
          amount: this.sum * this.sizecnt,
          buyer_email: "iamport@siot.do",
          buyer_name: "구매자이름",
          buyer_tel: "010-1234-5678",
          buyer_addr: "서울특별시 강남구 삼성동",
          buyer_postcode: "123-456",
        },
        function (rsp) {
          if (rsp.success) {
            var msg = "결제가 완료되었습니다.";
            msg += "\n고유ID : " + rsp.imp_uid;
            msg += "\n상점 거래ID : " + rsp.merchant_uid;
            msg += "\n결제 금액 : " + th.addComma(rsp.paid_amount)+"원";
            // msg += "카드 승인번호 : " + rsp.apply_num;

            axios
              .get(
                `${baseURL}/purchase/regist/${th.packPost}/${th.email}/${th.sum}/${th.sizecnt}`
              )
              .then((response) => {
                th.$router.push("/user/basket");
                th.$router.go();
                const Toast = Swal.mixin({
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  timer: 3000,
                  timerProgressBar: true,
                  onOpen: (toast) => {
                    toast.addEventListener("mouseenter", Swal.stopTimer);
                    toast.addEventListener("mouseleave", Swal.resumeTimer);
                  },
                });

                Toast.fire({
                  icon: "success",
                  title: `${msg}`,
                });
                setTimeout(() => {
                      th.$router.go();
                    }, 3000);
              })
              .catch((err) => {
                console.log(err);
              });
          } else {
            var msg = "결제에 실패하였습니다.";
            msg += "에러내용 : " + rsp.error_msg;
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 1000,
              timerProgressBar: true,
              onOpen: (toast) => {
                toast.addEventListener("mouseenter", Swal.stopTimer);
                toast.addEventListener("mouseleave", Swal.resumeTimer);
              },
            });

            Toast.fire({
              icon: "error",
              title: `${msg}`,
            });
          }
          // alert(msg);
        }
      );
    },
  },
  computed: {
    // Singleprice(price) {
    //   this.sum = 0;
    //   this.sum = this.prePosts.price;
    //   return this.sum;
    // },
    Packagingprice() {
      this.sum = 0;
      this.subsum = 0;
      this.discount = 0;
      if (this.prePosts.length == 1) {
        for (var i = 0; i < this.prePosts.length; i++) {
          this.sum += this.prePosts[i].price;
        }
      } else if (this.prePosts.length == 2) {
        for (var i = 0; i < this.prePosts.length; i++) {
          this.subsum += this.prePosts[i].price;
        }
        this.sum = Math.floor(this.subsum * 0.95);
        this.discount = 5;
      } else if (this.prePosts.length == 3) {
        for (var i = 0; i < this.prePosts.length; i++) {
          this.subsum += this.prePosts[i].price;
        }
        this.sum = Math.floor(this.subsum * 0.9);
        this.discount = 10;
      } else if (this.prePosts.length > 3) {
        for (var i = 0; i < this.prePosts.length; i++) {
          this.subsum += this.prePosts[i].price;
        }
        this.sum = Math.floor(this.subsum * 0.85);
        this.discount = 15;
      }
      return this.sum;
    },
  },
  data() {
    return {
      sizecnt: 1,
      sum: 0,
      discount: 0,
      packPost: [],
    };
  },
};
</script>

<style></style>
